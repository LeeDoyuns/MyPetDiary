package com.chunbae.mypetdiary.db.domain.pet

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.chunbae.mypetdiary.db.domain.user.Guardian
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

@Entity(tableName = "pet")
data class Pet (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "pet_id") var id: Long? = null,
    @ColumnInfo(name = "pet_name") var petName: String,
    @ColumnInfo(name = "birth_year") var birthYear: Int,
    @ColumnInfo(name =  "meet_date") var meetDate: String?,
    @ColumnInfo(name = "weight") @Nullable var weight: Double?,
    @ColumnInfo(name = "image") var image: ByteArray
){

    @Ignore
    constructor(petName: String, birthYear: Int, meetDate: String?, weight: Double?, image: ByteArray): this(null, petName, birthYear, meetDate, weight, image){
        this.petName = petName
        this.birthYear = birthYear
        this.weight = weight
        this.image = image
    }

}