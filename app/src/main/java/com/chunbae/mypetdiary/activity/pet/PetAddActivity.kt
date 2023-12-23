package com.chunbae.mypetdiary.activity.pet

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.app.ActivityCompat
import com.chunbae.mypetdiary.common.ImageTypeConverter
import com.chunbae.mypetdiary.common.code.CommonRequestCode
import com.chunbae.mypetdiary.common.DatePickerDialog
import com.chunbae.mypetdiary.common.activitys.BaseActivity
import com.chunbae.mypetdiary.databinding.ActivityPetAddBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.dao.PetDao
import com.chunbae.mypetdiary.db.domain.pet.Pet
import java.io.IOException

class PetAddActivity : BaseActivity<ActivityPetAddBinding>({ ActivityPetAddBinding.inflate(it)}) {

    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    lateinit var imgBase64Result: String
    val imageConverter: ImageTypeConverter = ImageTypeConverter()
    var imgUrl: Uri? = null
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var imgencodeByteArray: ByteArray //이미지를 DB에 넣기위한 변수

    private lateinit var db: AppRoomDatabase
    private lateinit var petDao: PetDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        btnBind()
        petDao = AppRoomDatabase.getDatabase(this).getPetDao()
        datePickerDialog = DatePickerDialog(this)
        datePickerDialog.initYear(binding.etBirthYear)
    }


    private fun btnBind() {
        //이미지 추가 버튼 이벤트
        binding.petImgAddBtn.setOnClickListener{

            if( ActivityCompat.checkSelfPermission(this, CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED){
                makeLongToast("카메라 권한을 허용하셔야 이용 가능합니다.")
                ActivityCompat.requestPermissions(this, arrayOf(CAMERA_PERMISSION), CommonRequestCode.CAMERA_REQUEST_CODE.code)
                return@setOnClickListener
            }
            confirmCaptureOrPickMessage()
        }

        //펫 정보 추가 이벤트
        binding.addPetBtn.setOnClickListener{
            savePetInfo()
        }

        //탄생년도
        binding.etBirthYear.setOnClickListener {

        }

        //만난 날
        binding.etMeetDate.setOnClickListener {
                datePickerDialog.showDatePickerDialog(binding.etMeetDate)
        }

    }

    fun savePetInfo(){
        val name: String = binding.etName.text.toString()
        val birth: Int = binding.etBirthYear.value
        val meetDate: String = binding.etMeetDate.text.toString()
        val tmpWeight = binding.etWeight.text.toString()
        val weight = if(tmpWeight.isNotEmpty()) tmpWeight.toDouble() else 0.0

        when {
            name.isEmpty() ->                   {makeShortToast("이름을 입력해주세요.")
                                                    return }
            birth <= 0         ->                 {makeShortToast("탄생 년도를 입력해주세요.")
                                                    return}
            //meetDate.isEmpty() -> makeShortToast("처음 만난 날을 입력해주세요.")
            imgencodeByteArray.isEmpty() ->     {makeShortToast("이미지를 등록해주세요.")
                                                    return}
        }
        Thread{
            val pet = Pet(name, birth, meetDate, weight, imgencodeByteArray)
            petDao.insertPetInfo(pet)
            runOnUiThread{
                makeShortToast("등록되었습니다.")
                finish()
            }
        }.start()


    }
    
    
    
    

    //이미지관련 처리 로직들

    fun confirmCaptureOrPickMessage() {
        var result = ""
        val options = arrayOf("사진 찍기", "앨범에서 선택하기")
        val builders = AlertDialog.Builder(this)
        builders.setTitle("이미지 선택")
            .setItems(options) { _, which ->
                // 사용자가 옵션을 선택한 경우
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // 사용자가 "Cancel" 버튼을 누른 경우
                dialog.dismiss()
            }
        builders.show()

        // 다이얼로그를 생성하고 보여줌
        /*
        val dialog = builder.create()
        dialog.show()
        val builder = AlertDialog.Builder(this)
            .setItems(options){ _, which ->
                when(which){
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
        builder.show()*/
    }
    fun openCamera() {
        createUri = createCameraUri()
            setMediaRequestListener { result ->
                imgUrl = Uri.parse(result)
                var bitmap = convertBitmap(imgUrl)
                //roomdatabase에 넣을 수 있게 타입 변환

                imgencodeByteArray = imageConverter.toByteArray(bitmap)
                binding.petImgAddBtn.setImageBitmap(bitmap)
            }
        takePicture.launch(createUri)
    }

    fun openGallery(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){  //티라미수 이후로는 pickmedia 사용
            setMediaRequestListener { result ->
                imgUrl = Uri.parse(result)
                var bitmap = convertBitmap(imgUrl)
                //roomdatabase에 넣을 수 있게 타입 변환

                imgencodeByteArray = imageConverter.toByteArray(bitmap)
                binding.petImgAddBtn.setImageBitmap(bitmap)
            }
            pickMedia.launch(PickVisualMediaRequest())
        }else{
            setMediaRequestListener { result ->
                imgUrl = Uri.parse(result)
                var bitmap = convertBitmap(imgUrl)
                //roomdatabase에 넣을 수 있게 타입 변환

                imgencodeByteArray = imageConverter.toByteArray(bitmap)
                binding.petImgAddBtn.setImageBitmap(bitmap)

            }
            imageRequest.launch("image/*")
        }
    }





    //Uri경로에 있는 파일을 bitmap으로 변환한다.
    private fun convertBitmap(uri: Uri?): Bitmap{
        var bitmap: Bitmap? = null
        if(uri != null){
            try{
                //P이후 코드는 ImageDecoder 사용
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    val source =
                        ImageDecoder.createSource(this.contentResolver, uri)
                    Log.d("MyPetDiaryLogs", "image source= ${source}")
                    bitmap = ImageDecoder.decodeBitmap(source)
                }else{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri)
                }
            }catch (e: IOException){
                e.printStackTrace()
            }
            Log.d("MyPetDiaryLogs", "convertBitmap = ${bitmap.toString()}")
        }
        return bitmap!!
    }

}