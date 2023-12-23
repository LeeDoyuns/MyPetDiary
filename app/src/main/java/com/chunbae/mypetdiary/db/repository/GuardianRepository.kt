package com.chunbae.mypetdiary.db.repository

import com.chunbae.mypetdiary.db.dao.GuardianDao
import com.chunbae.mypetdiary.db.domain.user.Guardian
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GuardianRepository(private val userDao: GuardianDao) {

    suspend fun getGuardianInfo(): List<Guardian> = withContext(Dispatchers.IO){
        userDao.getGuardianInfo()
    }
}