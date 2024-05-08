package com.chunbae.mypetdiary.db.domain.write

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.chunbae.mypetdiary.db.domain.pet.Pet
import com.chunbae.mypetdiary.db.domain.user.Guardian
import org.jetbrains.annotations.NotNull
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID

@Entity(tableName = "veterinary",
    foreignKeys =
        [ForeignKey(entity = Guardian::class, parentColumns = ["guardian_id"] , childColumns = ["guardian_id"]),
         ForeignKey(entity = Pet::class, parentColumns = ["pet_id"] , childColumns = ["pet_id"])
        ],
    indices = [Index(value = ["visit_date"], unique = false)]
    )
data class Veterinary(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "veterinary_id") val id: Long?,
    @ColumnInfo(name = "guardian_id")
    val guardian: Long,
    @ColumnInfo(name = "pet_id")
    val pet: Long,
    @ColumnInfo(name = "veterinary_name") @NotNull val veterinaryName: String,
    @ColumnInfo(name = "technician") @Nullable val technicianName: String?,
    @ColumnInfo(name = "pet_weight") @Nullable val weight: Float?,
    @ColumnInfo(name = "content") @Nullable val content: String?,
    @ColumnInfo(name = "etcMemo") @Nullable val etcMemo: String?,
    @ColumnInfo(name = "visit_date") @NotNull val visitDate: String

) {
    @Ignore
    constructor(guardian: Long,
                pet: Long,
                veterinaryName: String,
                technicianName: String?,
                weight: Float?,
                content: String?,
                etcMemo: String?,
                visitDate: String): this(
                    id = null,
                    guardian = guardian,
                    pet = pet,
                    veterinaryName = veterinaryName,
                    technicianName = technicianName,
                    weight = weight,
                    content = content,
                    etcMemo = etcMemo,
                    visitDate = visitDate
                )
}

@Entity(tableName = "vet_images",
    foreignKeys = [
        ForeignKey(entity = Veterinary::class, parentColumns = ["veterinary_id"], childColumns = ["vet_id"])
    ])
class Images(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "vet_image_id")
    val id: Long?,
    @ColumnInfo(name = "vet_id")
    val vet: Long,
    val img: ByteArray
){
    @Ignore
    constructor(
        vet: Long,
        img: ByteArray
    ): this(
        id = null,
        vet = vet,
        img = img
    )
}

@Entity(tableName = "vet_medicine",
    foreignKeys = [
        ForeignKey(entity = Veterinary::class, parentColumns = ["veterinary_id"], childColumns = ["vet_id"])
    ])
class VMedicine(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "v_medicine_id")
    val id: Long?,
    @ColumnInfo("vet_id")
    val vet: Long,
    val medicineName: String,
    val medicineVolume: Float?,
    val medicineUnit: String
){
}