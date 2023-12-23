package com.chunbae.mypetdiary.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.chunbae.mypetdiary.db.domain.pet.Pet

@Dao
interface PetDao {

    @Insert
    fun insertPetInfo(pet: Pet): Long

    @Query("select * from pet")
    fun selectAllPets(): List<Pet>

    @Query("delete from pet")
    fun deleteAllPets()
}