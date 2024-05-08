package com.chunbae.mypetdiary.db.domain.pet

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.chunbae.mypetdiary.db.domain.user.Guardian
import org.jetbrains.annotations.Nullable

@Entity(tableName = "pet",
        foreignKeys = [
            ForeignKey(entity = Guardian::class, parentColumns = ["guardian_id"], childColumns = ["guardian_id"])
        ]
    )
data class Pet(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "pet_id") val id: Long?,
    @ColumnInfo(name = "guardian_id")
    val guardianId: Long,
    @ColumnInfo(name = "pet_name") val petName: String,
    @ColumnInfo(name = "birth_year") val birthYear: Int,
    @ColumnInfo(name =  "meet_date") val meetDate: String?,
    @ColumnInfo(name = "weight") @Nullable val weight: Double?,
    @ColumnInfo(name = "image") val image: ByteArray
){

    @Ignore
    constructor(guardianId: Long,
                petName: String,
                birthYear: Int,
                meetDate: String?,
                weight: Double?,
                image: ByteArray): this(
                id = null,
                guardianId = guardianId,
                petName = petName,
                birthYear = birthYear,
                meetDate = meetDate,
                weight = weight,
                image = image
                )
}