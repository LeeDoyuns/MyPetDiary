package com.chunbae.mypetdiary.activity.pet

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.chunbae.mypetdiary.MainHomeFragment
import com.chunbae.mypetdiary.R
import com.chunbae.mypetdiary.activity.pet.fragment.FragmentEventListener
import com.chunbae.mypetdiary.activity.pet.fragment.PetListFragment
import com.chunbae.mypetdiary.activity.viewmodel.pet.VMPetList
import com.chunbae.mypetdiary.common.activitys.BaseActivity
import com.chunbae.mypetdiary.databinding.ActivityPetMainHomeBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.dao.PetDao
import com.chunbae.mypetdiary.db.domain.pet.Pet

/**
* 본격적인 메인화면.
* */
class PetMainHomeActivity : BaseActivity<ActivityPetMainHomeBinding>({ActivityPetMainHomeBinding.inflate(it) }), FragmentEventListener {

    private lateinit var vmPetList: VMPetList
    private lateinit var petDao: PetDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(binding.root)
        setFragment()
        setPetList()
        setBottomNavigationBarEvent()
    }

    //DB, ViewModel 초기화
    private fun init(){
        petDao = AppRoomDatabase.getDatabase(this).getPetDao()
        vmPetList = ViewModelProvider(this).get(VMPetList::class.java)
    }

    private fun setBottomNavigationBarEvent(){
       binding.bottomNavigationbar.setOnItemSelectedListener {
           when(it.itemId){
                R.id.ic_home -> {
                    true
                }
               R.id.ic_write -> {
                   true
               }
               R.id.ic_report -> {
                   true
               }

               else -> false
           }
       }
    }
    //Mainhome(달력이 있는 화면) fragment를 넣는다.
    private fun setFragment(){
        var frame: MainHomeFragment = MainHomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.contentFrame.id, frame)
            .commit()
    }

    private fun setPetList(){
        Thread{
            var petList: ArrayList<Pet>  = ArrayList(petDao.selectAllPets())
            Log.d("MyPetDiaryLogs", "petList => ${petList}")
            if(petList.isNotEmpty()){
                runOnUiThread {
                    vmPetList.setData(petList)
                }
            }

        }.start()
    }

    override fun onFragmentEvent(text: String, obj: Any?) {
        Log.d("MyPetDiaryLogs", "PetMainHomeAcvitiry = fragment start")
    }
}