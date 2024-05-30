package com.chunbae.mypetdiary.activity.write

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.result.PickVisualMediaRequest
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.chunbae.mypetdiary.R
import com.chunbae.mypetdiary.common.DatePickerDialog
import com.chunbae.mypetdiary.common.ImageTypeConverter
import com.chunbae.mypetdiary.common.activitys.BaseActivity
import com.chunbae.mypetdiary.common.code.CommonRequestCode
import com.chunbae.mypetdiary.databinding.ActivityWveterinaryDiaryBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.dao.GuardianDao
import com.chunbae.mypetdiary.db.dao.VeterinaryReportDao
import com.chunbae.mypetdiary.db.domain.user.Guardian
import com.chunbae.mypetdiary.db.domain.write.Images
import com.chunbae.mypetdiary.db.domain.write.VMedicine
import com.chunbae.mypetdiary.db.domain.write.Veterinary
import java.io.IOException
import java.util.Calendar

class WVeterinaryDiaryActivity : BaseActivity<ActivityWveterinaryDiaryBinding>({ ActivityWveterinaryDiaryBinding.inflate(it) }){
    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    val imageConverter: ImageTypeConverter = ImageTypeConverter()
    var imgUrl: Uri? = null
    private val imgList: MutableList<ByteArray> = mutableListOf()   //이미지 리스트
    private lateinit var  datePickerDialog: DatePickerDialog

    //db
    private lateinit var db: AppRoomDatabase
    private var selectedDate: String = ""
    private lateinit var guardianDao: GuardianDao
    private lateinit var vetDao: VeterinaryReportDao

    private var petId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSpinner(0)
        initComponent()
        btnClickEventSet()

        petId = intent.getLongExtra("petId", 0L)
        selectedDate = intent.getStringExtra("selectedDate")!!

    }
    private fun initComponent(){
        //방문날짜 기본값 지정. 당일.
        datePickerDialog = DatePickerDialog(this)
//        val cal = Calendar.getInstance()
//        var dateSplit = selectedDate.split("-")
//        cal.set(dateSplit[0].toInt(), dateSplit[1].toInt(), dateSplit[2].toInt())

        binding.etDate.setText(selectedDate)
    }
    //버튼클릭 이벤트
    private fun btnClickEventSet(){
        //방문날짜
        binding.btnCalendar.setOnClickListener { datePickerDialog.showDatePickerDialog(binding.etDate) }

        //이미지 첨부 버튼
        binding.btnAddImg.setOnClickListener {
            if( ActivityCompat.checkSelfPermission(this, CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED){
                makeLongToast("카메라 권한을 허용하셔야 이용 가능합니다.")
                ActivityCompat.requestPermissions(this, arrayOf(CAMERA_PERMISSION), CommonRequestCode.CAMERA_REQUEST_CODE.code)
                return@setOnClickListener
            }
            confirmCaptureOrPickMessage(this)
        }

        //save
        binding.btnVeterinarySave.setOnClickListener { save() }
    }

    //동물병원 방문기록 저장
    private fun save(){
        db = AppRoomDatabase.getInstance(this)!!
        guardianDao = db.getGuardianDao()
        vetDao = db.getVeterinaryReportDao()

        Thread{
            val guardian: List<Guardian> = guardianDao.getGuardianInfo()

            val etDate = binding.etDate.text.toString()
            val veterinaryName = binding.etVeterinary.text.toString()
            val vetName = binding.etVet.text.toString()
            val weight = binding.etWieght.text.toString().toFloat()
            val etcMemo = binding.etEtcMemo.text.toString()
            val content = binding.etMedicalRecords.text.toString()
            val record = Veterinary(
                guardian = guardian[0].id!!,
                pet = petId,
                veterinaryName = veterinaryName,
                technicianName = vetName,
                weight = weight,
                content = content,
                etcMemo = etcMemo,
                visitDate = etDate
            )
            Log.d("MyPetDiaryLogs", "record Veterinary Report => ${record.toString()}")
            val mGrp: LinearLayout = binding.grpMedicine
            val vetId = vetDao.insertVeterinaryReport(record)
            for (i in 0 until mGrp.childCount){
                val childGrp: LinearLayout = mGrp.get(i) as LinearLayout
                val medicineName = childGrp.getChildAt(0) as EditText
                val volume = childGrp.getChildAt(1) as EditText
                val medicineUnit = childGrp.getChildAt(2) as Spinner

                vetDao.insertVMedicine(VMedicine(
                    null,
                    vet = vetId,
                    medicineName = medicineName.text.toString(),
                    medicineVolume = volume.toString().toFloat(),
                    medicineUnit = medicineUnit.selectedItem.toString()
                    ))
                }

                //첨부이미지 저장
                for (i in 0 until imgList.size){
                    vetDao.insertVImages(Images(
                        vet = vetId,
                        img = imgList[i]
                    ))
                }
            runOnUiThread{
                makeShortToast("저장되었습니다.")
                finish()
            }

        }.start()





    }

    //spinner 관련 기능
    private fun setSpinner(idx: Int){
        val grp_medicine: LinearLayout = binding.grpMedicine
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.arr_unit, android.R.layout.simple_spinner_dropdown_item)
            val cLinear: LinearLayout = grp_medicine.getChildAt(idx) as LinearLayout //하위 linear layout
            val view: Spinner = cLinear.getChildAt(2) as Spinner
            if(view is Spinner){
                view.adapter = adapter
                view.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (view != null) {
                            Log.d("MyPetDiaryLogs", "선택 : ${position}, $id")
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
    }

    /** 
     * 이미지 관련 처리
     * */
    private fun confirmCaptureOrPickMessage(WVeterinaryDiaryActivity: WVeterinaryDiaryActivity) {

        //용량문제로 이미지는 6개까지만 등록
        if(imgList.size == 6) {
            makeLongToast("카메라 권한을 허용하셔야 이용 가능합니다.")
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA_PERMISSION), CommonRequestCode.CAMERA_REQUEST_CODE.code)
            return
        }

        val options = arrayOf("사진 찍기", "앨범에서 선택하기")
        val builders = AlertDialog.Builder(WVeterinaryDiaryActivity)
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
    }

    private fun openCamera() {
        createUri = createCameraUri()
        setMediaRequestListener { result ->
            imgUrl = Uri.parse(result)
            var bitmap = convertBitmap(imgUrl)
            //roomdatabase에 넣을 수 있게 타입 변환
            imgList.add(imageConverter.toByteArray(bitmap))
            addImgView(bitmap)
        }
        takePicture.launch(createUri)
    }

    private fun openGallery(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){  //티라미수 이후로는 pickmedia 사용
            setMediaRequestListener { result ->
                imgUrl = Uri.parse(result)
                var bitmap = convertBitmap(imgUrl)
                //roomdatabase에 넣을 수 있게 타입 변환
                imgList.add(imageConverter.toByteArray(bitmap))
                //하단에 이미지 미리보기 작게 보여줌.
                addImgView(bitmap)
            }
            pickMedia.launch(PickVisualMediaRequest())
        }else{
            setMediaRequestListener { result ->
                imgUrl = Uri.parse(result)
                var bitmap = convertBitmap(imgUrl)
                //roomdatabase에 넣을 수 있게 타입 변환

                imgList.add(imageConverter.toByteArray(bitmap))
                //하단에 이미지 미리보기 작게 보여줌.
                addImgView(bitmap)
            }
            imageRequest.launch("image/*")
        }
    }

    private fun convertBitmap(uri: Uri?): Bitmap {
        var bitmap: Bitmap? = null
        if(uri != null){
            try{
                //P이후 코드는 ImageDecoder 사용
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    val source =
                        ImageDecoder.createSource(this.contentResolver, uri)
                    bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.setTargetSize(200, 200)
                    }
                }else{
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                }
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        return bitmap!!
    }

    private fun addImgView(bitMap: Bitmap) {
        var imgView: ImageView = ImageView(this)
        var layoutParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParam.setMargins(10,0,10,0)
        layoutParam.width = 60
        layoutParam.height = 60
        imgView.scaleType = ImageView.ScaleType.CENTER_CROP

        imgView.setTag(if(imgList.size-1 < 0)0 else imgList.size)
        imgView.layoutParams = layoutParam
        imgView.setImageBitmap(bitMap)
        Log.d("MyPetDiaryLogs", "veterinary report ImageView = > ${bitMap.toString()}")

        //이미지 제거를 위한 onclickListener
        imgView.setOnClickListener { it ->
            imgList.removeAt(it.tag as Int)
            binding.vetFlexLayout.removeView(it)
        }
        binding.vetFlexLayout.addView(imgView)
    }


}