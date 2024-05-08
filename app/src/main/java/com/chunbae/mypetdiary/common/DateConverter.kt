package com.chunbae.mypetdiary.common

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class DateConverter {
    /**
     * 앱에서 companion object를 쓰는게 맞는 판단인지 모르겠음..
     * 나중에 알아보고 companion object로 변경할지 결정할 것.
     * Todo
     * */

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate): String {
        return value.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String): LocalDate {
        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)
        return value.let { LocalDate.parse(value, df) }
    }


}