package com.chunbae.mypetdiary.common

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.NumberPicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class DatePickerDialog(val context: Context) {
    val calendar: Calendar = Calendar.getInstance()


    fun showDatePickerDialog(etV: EditText) {
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val date: Int = calendar.get(Calendar.DATE)
        var result: String = ""

        try{
            val datePickerDialog: DatePickerDialog = DatePickerDialog(context, {
                    _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                var date = getDate()
                etV.setText(date)
            }, year, month, date )
            datePickerDialog.show()
        }catch (e: DiaryException){
            throw DiaryException(e.message.toString())
        }

    }

    fun getDate(): String {
        //선택된 날짜를 EditText에 표시
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        Log.d("MyPetDiaryLogs", "time => ${calendar.time}, now = ${Date()}")
        //Exception처리가 안돼서 이후날짜 선택 시 현재 날짜로 변경함.
        if(calendar.time > Date()){
            return sdf.format(Date())
        }
        return sdf.format(calendar.time)
    }

    fun initYear(numPicker: NumberPicker){
        val curYear: Int = calendar.get(Calendar.YEAR)
        numPicker.minValue = 1990
        numPicker.maxValue = curYear
        numPicker.value = curYear
    }


    class DiaryException (message: String) : IllegalArgumentException(message){

    }

}