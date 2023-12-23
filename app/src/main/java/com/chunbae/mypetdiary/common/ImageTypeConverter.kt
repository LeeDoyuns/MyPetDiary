package com.chunbae.mypetdiary.common

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class ImageTypeConverter {

    @TypeConverter
    fun toByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
        Log.d("MyPetDiaryLogs", "test = ${bitmap}")
        val wrapper = ContextWrapper(context)
        // 저장할 디렉토리 생성
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${System.currentTimeMillis()}.png")
        try {
            // 이미지 파일로 저장
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // 저장한 파일의 Uri를 반환
        return Uri.parse(file.absolutePath)
    }

}