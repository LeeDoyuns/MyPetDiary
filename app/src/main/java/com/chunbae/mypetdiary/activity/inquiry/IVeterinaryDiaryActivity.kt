package com.chunbae.mypetdiary.activity.inquiry

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.isVisible
import com.chunbae.mypetdiary.common.activitys.BaseActivity
import com.chunbae.mypetdiary.databinding.ActivityIveterinaryDiaryBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.domain.write.Images
import com.chunbae.mypetdiary.db.domain.write.VMedicine
import com.chunbae.mypetdiary.db.domain.write.Veterinary

class IVeterinaryDiaryActivity : BaseActivity<ActivityIveterinaryDiaryBinding>({ActivityIveterinaryDiaryBinding.inflate(it)}) {

    private lateinit var date: String

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
            val vetId = veterinary?.map { vet -> vet.id }?.toList()
            if (!vetId.isNullOrEmpty()) {
                val medicine = vetDao.selectVMedicineList(vetId)
                val images = vetDao.selectVImageList(vetId)
                //조회한 데이터들로 화면에 set
                setViewData(veterinary, medicine, images)
            }else {
                binding.emptyData.isVisible = true
                binding.layoutMain.isVisible = false
            }
        }.start()
    }

    private fun setViewData(veterinary: List<Veterinary>
                            , medicine: List<VMedicine>?
                            , images: List<Images>?){
        veterinary.forEachIndexed{idx, vet ->
            if(idx == 0){
                binding.tvDate.setText(vet.visitDate)
            }else{
                addVeterinaryContents(idx, vet)
            }
        }


    }

    private fun addVeterinaryContents(idx: Int, vet: Veterinary){

    }

    private fun btnClickEventSet(){

    }
}