package com.chunbae.mypetdiary.activity.write

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.chunbae.mypetdiary.R
import com.chunbae.mypetdiary.common.DatePickerDialog
import com.chunbae.mypetdiary.common.ImageTypeConverter
import com.chunbae.mypetdiary.common.activitys.BaseActivity
import com.chunbae.mypetdiary.databinding.ActivityPetAddBinding
import com.chunbae.mypetdiary.databinding.ActivityPetMainHomeBinding
import com.chunbae.mypetdiary.databinding.ActivityUserJoinBinding
import com.chunbae.mypetdiary.databinding.ActivityVeterinaryDiaryBinding
import com.chunbae.mypetdiary.db.AppRoomDatabase
import com.chunbae.mypetdiary.db.dao.PetDao

class VeterinaryDiaryActivity : BaseActivity<ActivityVeterinaryDiaryBinding>({ ActivityVeterinaryDiaryBinding.inflate(it) }){
    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    lateinit var imgBase64Result: String
    val imageConverter: ImageTypeConverter = ImageTypeConverter()
    var imgUrl: Uri? = null
    private lateinit var imgencodeByteArray: ByteArray //이미지를 DB에 넣기위한 변수

    private lateinit var db: AppRoomDatabase
    private lateinit var petDao: PetDao
    private lateinit var  datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSpinner(0)
        initComponent()

        btnClickEventSet()

    }
    fun initComponent(){
        //방문날짜 기본값 지정. 당일.
        datePickerDialog = DatePickerDialog(this)
        binding.etDate.setText(datePickerDialog.getDate().toString())
    }
    //버튼클릭 이벤트
    fun btnClickEventSet(){
        //방문날짜
        binding.btnCalendar.setOnClickListener { datePickerDialog.showDatePickerDialog(binding.etDate) }
        //ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ
    }

    //캘린더



    //spinner 관련 기능
    private fun setSpinner(idx: Int){
        val grp_medicine: LinearLayout = binding.grpMedicine
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.arr_unit, android.R.layout.simple_spinner_dropdown_item)
//            .also { arrayAdapter ->
//                arrayAdapter.setDropDownViewResource(R.layout.spinner_layout)
//            }
//        for(i in 0 until grp_medicine.childCount){
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
//        }

    }

}