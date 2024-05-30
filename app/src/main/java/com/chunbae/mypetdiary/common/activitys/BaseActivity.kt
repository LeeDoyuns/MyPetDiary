package com.chunbae.mypetdiary.common.activitys

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.viewbinding.ViewBinding
import com.chunbae.mypetdiary.R
import com.chunbae.mypetdiary.common.DpToPixelConverter
import com.chunbae.mypetdiary.common.ImageTypeConverter
import com.chunbae.mypetdiary.db.domain.pet.Pet
import com.google.android.flexbox.FlexboxLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

abstract class BaseActivity<T: ViewBinding> (
    val bindingFactory: (LayoutInflater) -> T
): AppCompatActivity(){
    private var _binding: T? = null
    lateinit var imgB64Encoding: String
    val binding get() = _binding!!
    var callback: ((String) -> Unit)? = null
    var createUri: Uri? = null
    lateinit var imageTypeConverter: ImageTypeConverter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        imageTypeConverter = ImageTypeConverter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    open fun btnClick(){}

    open fun makeShortToast(str: String){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
    open fun makeLongToast(str: String){
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }
    open fun Activity.startIntent(destination: Class<out Activity>){
        val intent = Intent(this, destination)
        startActivity(intent)
    }

    /*특정 뷰 하위 뷰까지복사*/

    open fun copyViewOrLayout(
        context: Context,
        parentLayout: ViewGroup,
    ) {
        val specificView = LinearLayout(context)
        specificView.layoutParams = parentLayout.layoutParams
        specificView.orientation = (specificView as? LinearLayout)
            ?.orientation ?: LinearLayout.VERTICAL

        parentLayout.children.forEach { child ->
            val copiedChild = when(child) {
                is ViewGroup -> copyViewOrLayout(context, child)
                is View -> {
                    val copiedView = View(context)
                    copiedView.layoutParams = child.layoutParams
                    copiedView
                }
                else -> View(context)
            }
            copiedChild.let { parentLayout.addView(it as View) }
        }
    }


    /*카메라 관련 설정*/
    val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()){ success: Boolean ->
        if(success){
            //촬영 후
            createUri?.let { imageCrop(it) }
        }else{
            makeShortToast("사진 촬영에 실패했습니다.")
        }
    }

    val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data ?: Uri.fromFile(File(createUri.toString()))
            imageCrop(uri)
        }
    }

    fun imageCrop(uri: Uri){
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .setFixAspectRatio(true)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val croppedUri = result.uri
                Log.d("MyPetDiaryLogs", "crop reulst = >${result.uri}")
                callback?.invoke(croppedUri.toString())
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                runOnUiThread{
                    makeShortToast("이미지 처리중 오류가 발생했습니다.")
                    finish()
                }
            }
        }
    }


    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
          it?.let { uri ->
              imageCrop(uri)
          }
      }
    val imageRequest = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let {
            if(uri == null){
                makeShortToast("이미지 파일을 선택해주세요.")
            }
            imageCrop(uri)
        }
    }
    //callback 함수
    open fun setMediaRequestListener(callback: (String) -> Unit){
        this.callback = callback
    }

    private fun convertUriToBitmap(uri: Uri): ByteArray{
        var ins = applicationContext.contentResolver.openInputStream(uri)

        val img: Bitmap = BitmapFactory.decodeStream(ins)
        ins?.close()
        val resized = Bitmap.createScaledBitmap(img, 256, 256, true)
        val byteArrOs = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.PNG, 60, byteArrOs)
        return byteArrOs.toByteArray()
    }

    fun createCameraUri(): Uri {
        // Create a file to save the image
        val file = createImageFile()

        return FileProvider.getUriForFile(
            this,
            "com.chunbae.mypetdiary.fileprovider",
            file
        )
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(null)

        return File.createTempFile(
            "MyPet_${System.currentTimeMillis()}_",
            ".png",
            storageDir
        )
    }

    /*카메라 관련 설정 end*/

    open fun makeCircleImageButton(pet: Pet): CircleImageView {
        var circleButton: CircleImageView = CircleImageView(this)
        var bitmap: Bitmap = imageTypeConverter.toBitmap(pet.image)

        // dp를 픽셀로 변환
        var pixcels = DpToPixelConverter().convert(resources, 70)
        var mPixcel = DpToPixelConverter().convert(resources, 10)

        // 레이아웃 파라미터가 null이면 새로 생성
        val newLayoutParams: FlexboxLayout.LayoutParams = FlexboxLayout.LayoutParams(pixcels, pixcels)

        newLayoutParams.width = pixcels
        newLayoutParams.height = pixcels
        newLayoutParams.setMargins(mPixcel, mPixcel, mPixcel, mPixcel)

        circleButton.setImageBitmap(bitmap)
        circleButton.scaleType = ImageView.ScaleType.CENTER_CROP
        circleButton.layoutParams = newLayoutParams
        circleButton.borderColor =  ContextCompat.getColor(this, R.color.gray)
        circleButton.borderWidth = 5

        return circleButton
    }


}

