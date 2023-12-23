package com.chunbae.mypetdiary.activity.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chunbae.mypetdiary.R
import com.chunbae.mypetdiary.activity.pet.PetListMainActivity
import com.chunbae.mypetdiary.common.code.CommonRequestCode
import com.chunbae.mypetdiary.common.activitys.BaseActivity
import com.chunbae.mypetdiary.databinding.ActivityLoadingBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.dao.GuardianDao
import com.chunbae.mypetdiary.db.domain.user.Guardian
import com.chunbae.mypetdiary.db.repository.GuardianRepository
import com.chunbae.mypetdiary.db.viewmodel.GuardianViewModel
import com.chunbae.mypetdiary.db.viewmodel.GuardianViewModelFactory

/**
 * 로딩 화면에서 유저 여부 조회 후 등록화면 혹은 펫 메인화면으로 이동하는것으로 변경.
 */
class LoadingActivity : BaseActivity<ActivityLoadingBinding>({ActivityLoadingBinding.inflate(it)}){
    private lateinit var guardian: ArrayList<Guardian>

    private val REQUIRED_PERMISSIONS: Array<String> = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.CAMERA)
    }else{
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }
    lateinit var db: AppRoomDatabase
    lateinit var guardianDao: GuardianDao
    lateinit var guardianViewModel: GuardianViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //권한 체크
        checkPermissions()
        setContentView(R.layout.activity_loading)
    }

    fun getInstance(context: Context){
        AppRoomDatabase.getInstance(this)
        guardianDao = AppRoomDatabase.getDatabase(this).getGuardianDao()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CommonRequestCode.MEDIA_REQUEST_CODE.code -> {
                if(grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }){
                    getInstance(this)
                    checkAlreadyUser()
                }else{
                    runOnUiThread{
                        makeShortToast("권한 동의 후 이용하실 수 있습니다.")
                        finish()
                    }
                }
            }
        }
    }

    private fun checkPermissions(){
        Log.d("MyPetDiaryLogs", "권한 체크")
        var permissions: Boolean = true

        for (permission in REQUIRED_PERMISSIONS){
            permissions = ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
            if(permissions){
                ActivityCompat.requestPermissions(this,
                    REQUIRED_PERMISSIONS,
                    CommonRequestCode.MEDIA_REQUEST_CODE.code
                )
            }else{
                getInstance(this)
                checkAlreadyUser()
            }
        }

    }

    private fun checkAlreadyUser() {
        val pgrBar: ProgressBar = findViewById(R.id.pg_bar)
        pgrBar.visibility = View.VISIBLE

        val guardianRepo = GuardianRepository(guardianDao)
        val guardianVMF = GuardianViewModelFactory(guardianRepo)
        guardianViewModel = ViewModelProvider(this, guardianVMF).get(GuardianViewModel::class.java)
        guardianViewModel.getUserInfo()

        guardianViewModel.guardian.observe(this, Observer{
            guardian = ArrayList(it)
            if(it.size > 0){
                moveMainHome()
            }else {
                moveUserMain()
            }
        })

    }

    private fun moveMainHome() {
        val intent = Intent(this, PetListMainActivity::class.java)
        intent.putExtra("nickname", guardian?.get(0)?.nickname)
        intent.putExtra("id", guardian?.get(0)?.id)
        startActivity(intent)
        finish()
    }

    private fun moveUserMain(){
        val intent = Intent(this, NewUserMainActivity::class.java)
        startActivity(intent)
        finish()
    }
}