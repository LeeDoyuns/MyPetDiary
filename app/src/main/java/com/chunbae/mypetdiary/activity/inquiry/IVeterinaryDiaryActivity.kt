package com.chunbae.mypetdiary.activity.inquiry

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.isVisible
import com.chunbae.mypetdiary.common.ImageTypeConverter
import com.chunbae.mypetdiary.common.activitys.BaseActivity
import com.chunbae.mypetdiary.databinding.ActivityIveterinaryDiaryBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.domain.write.Images
import com.chunbae.mypetdiary.db.domain.write.VMedicine
import com.chunbae.mypetdiary.db.domain.write.Veterinary

class IVeterinaryDiaryActivity : BaseActivity<ActivityIveterinaryDiaryBinding>({ActivityIveterinaryDiaryBinding.inflate(it)}) {

    private lateinit var date: String
    private val imageConverter: ImageTypeConverter = ImageTypeConverter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setContentView(binding.root)
        initData()

        btnClickEventSet()

    }

    //날짜 기준으로 데이터 불러오기
    private fun initData() {
        val date: String = intent.getStringExtra("date")!!
        val petId = intent.getLongExtra("petId", 0L)
        val db = AppRoomDatabase.getDatabase(this)
        var guardianId: Long? = 0L
        val vetDao = db.getVeterinaryReportDao()

        //하루에 여러개 입력 할 수도...
        Thread{
            guardianId = db.getGuardianDao().getGuardianInfo()[0].id
            val veterinary: List<Veterinary>? = vetDao.selectVeterinaryReports(date, petId)
            Log.d("MyPetDiaryLogs", "guardianId=>$guardianId, veterinaryList=> $veterinary")
            val vetId = veterinary?.map { vet -> vet.id }?.toList()
            if (!vetId.isNullOrEmpty()) {
                val medicine = vetDao.selectVMedicineList(vetId)
                val images = vetDao.selectVImageList(vetId)
                //조회한 데이터들로 화면에 set
                setViewData(veterinary, medicine, images)
            }else {
                binding.emptyData.isVisible = true
                binding.layoutScroll.isVisible = false
            }
        }.start()
    }

    private fun setViewData(veterinary: List<Veterinary>
                            , medicine: List<VMedicine>?
                            , images: List<Images>?){
        veterinary.forEachIndexed{idx, vet ->
            if(idx == 0){   //한개만 있으면 그냥 셋팅..
                setVeterinayDiaryData(vet, images, medicine)
            }else{  //하루에 한개 이상일경우..
                addVeterinaryContents(idx, vet, images, medicine)
            }
        }
    }

    //동적으로 동물병원 방문일지 추가
    private fun addVeterinaryContents(
        idx: Int,
        vet: Veterinary,
        images: List<Images>?,
        medicine: List<VMedicine>?,
        ){
        copyViewOrLayout(this, binding.layoutScroll)
        setVeterinayDiaryData(vet, images, medicine)
    }

    private fun setVeterinayDiaryData(
        vet: Veterinary,
        images: List<Images>?,
        medicines: List<VMedicine>?
    ) {
        binding.etDate.text = vet.visitDate
        binding.etVeterinary.text = vet.veterinaryName
        binding.etVet.text = vet.technicianName
        binding.etWieght.text = vet.weight.toString()
        binding.etMedicalRecords.text = vet.content
        if(!images.isNullOrEmpty()) addImages(images.filter{
            it.vet == vet.id
        }.toList())
        if(!medicines.isNullOrEmpty()) addMedicines(medicines.filter{
            it.vet == vet.id
        }.toList())
        binding.etEtcMemo.text = vet.etcMemo
    }

    private fun btnClickEventSet(){

    }

    private fun addMedicines(medicines: List<VMedicine>){
        val lLayout = binding.listMedicine
        medicines.forEach { m ->
            val medicineName: TextView = TextView(this)
            val medicineVolume: TextView = TextView(this)
            medicineName.minWidth = 130
            medicineName.textSize = 13.0F
            medicineVolume.minWidth = 50
            medicineVolume.textSize = 13.0F

            medicineName.text = m.medicineName
            medicineVolume.text = "${m.medicineVolume} ${m.medicineUnit}"
            lLayout.addView(medicineName)
            lLayout.addView(medicineVolume)
        }
    }

    private fun addImages(images: List<Images>) {
        val flxLayout = binding.vetFlexLayout
        images.forEach { img ->
            val bitmapImg = imageConverter.toBitmap(img.img)
            val imgView: ImageView = ImageView(this)
            val layoutParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParam.setMargins(10, 0, 10, 0)
            layoutParam.width = 60
            layoutParam.height = 60
            imgView.scaleType = ImageView.ScaleType.CENTER_CROP

            imgView.setTag(if (images.size - 1 < 0) 0 else images.size)
            imgView.layoutParams = layoutParam
            imgView.setImageBitmap(bitmapImg)

            flxLayout.addView(imgView)
        }
    }
}