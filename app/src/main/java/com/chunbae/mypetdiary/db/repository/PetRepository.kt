package com.chunbae.mypetdiary.db.repository

import com.chunbae.mypetdiary.db.dao.PetDao
import com.chunbae.mypetdiary.db.domain.pet.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PetRepository(private val petDao: PetDao) {
    suspend fun selectPetList(): List<Pet>  = withContext(Dispatchers.IO){
        petDao.selectAllPets()
    }
}