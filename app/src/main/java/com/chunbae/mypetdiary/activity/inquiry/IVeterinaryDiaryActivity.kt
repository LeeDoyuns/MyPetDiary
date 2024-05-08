package com.chunbae.mypetdiary.activity.inquiry

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chunbae.mypetdiary.R
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
        var medicine: List<VMedicine>?
        var images: List<Images>?

        //하루에 여러개 입력 할 수도...
        Thread{
            guardianId = db.getGuardianDao().getGuardianInfo()[0].id
            val veterinary: List<Veterinary>? = vetDao.selectVeterinaryReports(date, petId)
            if (veterinary != null) {
                for(i in 0 until veterinary.size){
                    medicine = vetDao.selectVMedicineList(veterinary[i].id!!)
                    images = vetDao.selectVImageList(veterinary[i].id!!)
                }
            }

        }.start()

    }

    private fun btnClickEventSet(){

    }
}