package com.chunbae.mypetdiary.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.chunbae.mypetdiary.db.domain.pet.Pet
import com.chunbae.mypetdiary.db.domain.user.Guardian
import com.chunbae.mypetdiary.db.dto.PetAndGuardian

@Dao
interface GuardianDao {

    @Query("SELECT * FROM guardian")
    fun getGuardianInfo(): List<Guardian>

    @Insert
    fun insertGuardian(guardian: Guardian): Long

    @Delete
    fun deleteGuardian(guardian: Guardian)

    @Query("delete from guardian")
    fun deleteAllUsers()


}