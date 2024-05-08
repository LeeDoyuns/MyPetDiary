package com.chunbae.mypetdiary.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.chunbae.mypetdiary.common.CommonBcrypt
import com.chunbae.mypetdiary.databinding.ActivityUserJoinBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.domain.user.Guardian

class UserJoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserJoinBinding

    //전역변수로 사용
    private lateinit var userId: String
    private lateinit var password: String
    private lateinit var nickname: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUserSave.setOnClickListener {
            userId = binding.etJoinId.text.toString()
            password = binding.etJoinPassword.text.toString()
            nickname = binding.etJoinNickname.text.toString()

            checkValid()
        }
    }

    private fun checkValid(){
        if(userId.isEmpty()|| password.isEmpty() || nickname.isEmpty()){
            var message = "";
            //토스트메세지 return
            if(userId.isEmpty()){
                message = "계정 ID를 입력해주세요.";
            }else if(password.isEmpty()){
                message = "비밀번호를 입력해주세요."
            }else{
                message = "닉네임을 입력해주세요."
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            return
        }
        save()
    }

    private fun save(){

        //password암호화
        var bcryptPswd: String = CommonBcrypt.encryptPassword(password)

        //roomDB에 저장
        Thread{
            var user = Guardian(
                userId = userId,
                nickname = nickname,
                password = password
            )
            var guardian = AppRoomDatabase.getDatabase(this).getGuardianDao().insertGuardian(user)
            
            //서버 저장로직 추가예정TODO
            
            runOnUiThread{
                Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()

    }


}