package com.chunbae.mypetdiary.activity.pet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.chunbae.mypetdiary.R
import com.chunbae.mypetdiary.activity.pet.fragment.FragmentEventListener
import com.chunbae.mypetdiary.activity.pet.fragment.PetListFragment
import com.chunbae.mypetdiary.activity.viewmodel.pet.VMPetList
import com.chunbae.mypetdiary.common.DpToPixelConverter
import com.chunbae.mypetdiary.common.activitys.BaseActivity
import com.chunbae.mypetdiary.common.code.CommonStringCode
import com.chunbae.mypetdiary.databinding.ActivityPetListMainBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.dao.PetDao
import com.chunbae.mypetdiary.db.domain.pet.Pet
import de.hdodenhof.circleimageview.CircleImageView

class PetListMainActivity : BaseActivity<ActivityPetListMainBinding>({ActivityPetListMainBinding.inflate(it)}), FragmentEventListener{

    private lateinit var petDao: PetDao
    private lateinit var petListFrgment: PetListFragment
    private lateinit var vmPetList: VMPetList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        petDao = AppRoomDatabase.getDatabase(this).getPetDao()
        setContentView(binding.root)
        setBtnClickEvent()//테스트용 버튼(전체 삭제)

        var intent = intent
        if(intent.hasExtra("nickname")){
            binding.nickname.setText("${intent.getStringExtra("nickname")}님 어서오세요.")
        }

        //fragment관련 제어
        petListFrgment = PetListFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, petListFrgment)
            .commit()
        vmPetList = ViewModelProvider(this).get(VMPetList::class.java)
    }

    override fun onRestart() {
        super.onRestart()
        petListFrgment = PetListFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, petListFrgment)
            .commit()
        vmPetList = ViewModelProvider(this).get(VMPetList::class.java)
    }


    private fun selectPet(petId: Long){
        var intent:Intent = Intent(this, PetMainHomeActivity::class.java)
        intent.putExtra("petId", petId)
        startActivity(intent)
        finish()
    }


    private fun setBtnClickEvent() {
        binding.btnDelPet.setOnClickListener {
            Thread{
                petDao.deleteAllPets()
            }.start()
        }
    }

    private fun movePetAddPage(){
        val intent = Intent(this, PetAddActivity::class.java)
        startActivity(intent)
    }

    override fun onFragmentEvent(text: String, obj: Any?) {
        if(text === CommonStringCode.SEL_PET.value){
            selectPet(obj as Long)
        }else if(text === CommonStringCode.ADD_PET.value){
            movePetAddPage()
        }
    }
}