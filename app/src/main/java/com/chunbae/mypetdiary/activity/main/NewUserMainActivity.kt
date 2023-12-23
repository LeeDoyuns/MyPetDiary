package com.chunbae.mypetdiary.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.chunbae.mypetdiary.activity.pet.PetListMainActivity
import com.chunbae.mypetdiary.activity.user.UserJoinActivity
import com.chunbae.mypetdiary.common.activitys.BaseActivity
import com.chunbae.mypetdiary.databinding.ActivityNewUserMainBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.dao.GuardianDao
import com.chunbae.mypetdiary.db.dao.PetDao
import com.chunbae.mypetdiary.db.domain.user.Guardian

class NewUserMainActivity : BaseActivity<ActivityNewUserMainBinding>({ActivityNewUserMainBinding.inflate(it)}) {

    private lateinit var guardian: ArrayList<Guardian>

    lateinit var db: AppRoomDatabase
    lateinit var guardianDao: GuardianDao
    lateinit var petDao: PetDao

    fun getInstance(context: Context){
        db = AppRoomDatabase.getInstance(context)!!
        guardianDao = db.getGuardianDao()
        petDao = db.getPetDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance(this@NewUserMainActivity)
        checkAlreadyUser()
    }




    override fun onRestart() {
        checkAlreadyUser()
        Log.d("MyPetDiaryLogs", "restart")
        super.onRestart()
    }

    private fun checkAlreadyUser(){
        /*
       *기존 유저가 있는지 확인 후 있다면 Home으로 이동시킨다.
       * */
        Thread{
            guardian = ArrayList(guardianDao.getGuardianInfo())
            Log.d("LOG", guardian.size.toString())
            if(guardian.size > 0){
                moveMainHome()
            }else{
                setJoinBtnClickEvent()
            }
        }.start()

    }

    private fun moveMainHome() {
        val intent = Intent(this, PetListMainActivity::class.java)
        intent.putExtra("nickname", guardian?.get(0)?.nickname)
        intent.putExtra("id", guardian?.get(0)?.id)
        startActivity(intent)
        finish()
    }

    private fun setJoinBtnClickEvent (){
        val btn:ImageButton = binding.joinBtn
        btn.setOnClickListener { it ->
            moveJoinUserPage()
        }

        val delBtn: Button = binding.delBtn
        delBtn.setOnClickListener{
            Thread{
                guardianDao.deleteAllUsers()
                runOnUiThread{
                    Toast.makeText(this, "삭제완료.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
    }

    private fun moveJoinUserPage() {
        val intent = Intent(this, UserJoinActivity::class.java)
        startActivity(intent)
    }
}