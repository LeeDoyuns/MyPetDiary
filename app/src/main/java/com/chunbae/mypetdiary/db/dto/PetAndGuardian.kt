package com.chunbae.mypetdiary.db.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.chunbae.mypetdiary.db.domain.pet.Pet
import com.chunbae.mypetdiary.db.domain.user.Guardian

/*
* join이 필요한 부분?
* */
data class PetAndGuardian (
    @Embedded val guardian: Guardian,
    @Relation(
        parentColumn = "id",
        entityColumn = "guardianId"
    )
    val pet: List<Pet>
){
}