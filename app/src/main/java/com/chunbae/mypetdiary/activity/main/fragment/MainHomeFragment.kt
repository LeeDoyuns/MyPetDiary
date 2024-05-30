package com.chunbae.mypetdiary.activity.main.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.chunbae.mypetdiary.R
import com.chunbae.mypetdiary.activity.inquiry.IVeterinaryDiaryActivity
import com.chunbae.mypetdiary.activity.pet.fragment.FragmentEventListener
import com.chunbae.mypetdiary.activity.pet.fragment.PetListFragment
import com.chunbae.mypetdiary.activity.write.WVeterinaryDiaryActivity
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainHomeFragment : Fragment() {

    private lateinit var root: View
    private var eventListener: FragmentEventListener? = null
    private lateinit var context: Context
    private var recevedPetId: Long = 0L
    private lateinit var selectedDate: String
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_main_home, container, false)

        if(arguments?.getLong("petId") != null){
            recevedPetId = (arguments?.getLong("petId"))!!
        }
        return root
    }
    //Activity에서 Fragment를 호출 할 때 실행되는 메서드
    override fun onAttach(context: Context) {
        super.onAttach(context)
            this.context = context
        if (context is FragmentEventListener) {
            eventListener = context
        }else {
            throw ClassCastException("$context must implement OnButtonClickListener")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val petListFgr = PetListFragment()
        addChildrenFragment(petListFgr)
        setCalendar()

    }
    //MainHomeFragment에서는 petList를 보여준다.
    private fun addChildrenFragment(fragment: PetListFragment){
        val fragmentManager = childFragmentManager

        val bundle = Bundle()
        bundle.putLong("petId", recevedPetId)
        fragment.arguments = bundle

        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frg_petlist, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    //기존에 일정이 존재하는 데이터를 setting함.
    private fun setCalendar(){
        var calendar = root.findViewById<MaterialCalendarView>(R.id.calendar_view)
        val cal = Calendar.getInstance()

        //Long click -> 등록팝업
        calendar.setOnDateLongClickListener { widget, date ->
            Log.d("MyPetDiaryLogs", "sedlectedDate = $date")
            cal.set(date.year, date.month-1, date.day)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(cal.time)
            Log.d("MyPetDiaryLogs", "selectedDate = > $selectedDate")
            //write
            setPopupMenu("write", widget)
        }

        //short click -> 조회팝업
        calendar.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            cal.set(date.year, date.month-1, date.day)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(cal.time)
            Log.d("MyPetDiaryLogs", "selectedDate = > $selectedDate")

            //inquire
            setPopupMenu("inquire", widget)
        })

    }

    private fun setPopupMenu(type: String, view: View){
        val layout = WindowManager.LayoutParams()
        layout.width = WindowManager.LayoutParams.WRAP_CONTENT
        layout.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog = AlertDialog.Builder(context)
            .setView(R.layout.menu_select_pop)
            .show()
            .also { dialog ->
                if(dialog == null) return@also

                val tv_tvView = dialog.findViewById<TextView>(R.id.tv_date)
                val vVeterinary: Button = dialog.findViewById(R.id.btn_veterinary)
                val tMedicine: Button = dialog.findViewById(R.id.btn_medicine)
                val symptom: Button = dialog.findViewById(R.id.btn_symptom)
                val etc: Button = dialog.findViewById(R.id.btn_etc)

                when(type){
                    //작성하기
                    "write" -> {
                        vVeterinary.setOnClickListener { writeVisitVeterinary() }
                        tMedicine.setOnClickListener { writeTakeMedicine() }
                        symptom.setOnClickListener { writeSymptom() }
                        etc.setOnClickListener { writeEtcMemo() }
                    }
                    //조회팝업
                    else -> {
                        vVeterinary.setOnClickListener { inquireVisitVeterinary() }
                        tMedicine.setOnClickListener { inquireTakeMedicine() }
                        symptom.setOnClickListener { inquireSymptom() }
                        etc.setOnClickListener { inquireEtcMemo() }
                    }
                }
                tv_tvView.text = selectedDate
            }
        dialog.window?.attributes = layout
        dialog.show()
    }

    private fun inquireEtcMemo() {
        TODO("Not yet implemented")
    }

    private fun inquireSymptom(){

    }
    private fun inquireTakeMedicine() {
        TODO("Not yet implemented")
    }

    private fun inquireVisitVeterinary() {
        var intent = Intent(context, IVeterinaryDiaryActivity::class.java)
        intent.putExtra("date", selectedDate)
        intent.putExtra("petId", recevedPetId)
        startActivity(intent)
        Log.d("MyPetDiaryLogs", "inquiry veterinaryDiary")
        dialog.dismiss()
    }

    private fun writeEtcMemo() {
        Log.d("MyPetDiaryLogs", "writeEtcMemo")
    }

    private fun writeSymptom() {
        Log.d("MyPetDiaryLogs", "writeSymptom")
    }

    private fun writeVisitVeterinary(){
        var intent:Intent = Intent(context, WVeterinaryDiaryActivity::class.java)
        intent.putExtra("petId", recevedPetId)
        intent.putExtra("selectedDate", selectedDate)
        startActivity(intent)
        dialog.dismiss()
        Log.d("MyPetDiaryLogs", "writeVisitVeterinary")

    }
    private fun writeTakeMedicine(){
        Log.d("MyPetDiaryLogs", "writeTakeMedicine")
    }


}