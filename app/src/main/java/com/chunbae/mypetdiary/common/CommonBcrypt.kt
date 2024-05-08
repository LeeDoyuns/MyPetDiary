package com.chunbae.mypetdiary.common

import android.util.Base64
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
/**
 * 암호화를 위한 class
 */
class CommonBcrypt {

    companion object {
        fun encryptPassword(pswd: String): String{
            try{
                val SECRET_IV = byteArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
                val SECRET_KeY = "ABCDEFGH12345678"

                val iv = IvParameterSpec(SECRET_IV)
                val key = SecretKeySpec(SECRET_KeY.toByteArray(), "AES")

                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, key, iv)
                val crypted = cipher.doFinal(pswd.toByteArray())
                val encodedByte = Base64.encode(crypted, Base64.DEFAULT)

                return String(encodedByte)

            }catch(e: Exception){
                e.printStackTrace()
            }
            return ""
        }
    }
}