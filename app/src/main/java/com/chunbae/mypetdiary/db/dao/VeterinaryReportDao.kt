package com.chunbae.mypetdiary.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.chunbae.mypetdiary.db.domain.write.Images
import com.chunbae.mypetdiary.db.domain.write.VMedicine
import com.chunbae.mypetdiary.db.domain.write.Veterinary
import java.util.UUID

@Dao
interface VeterinaryReportDao {
    @Insert
    fun insertVeterinaryReport(vet: Veterinary): Long

    // 약물처방내역
    @Insert
    fun insertVMedicine(medicine: VMedicine): Long

    // 이미지 첨부
    @Insert
    fun insertVImages(img: Images): Long

    // 동물병원 기록 조회
    @Query("select * from veterinary where pet_id = :petId and visit_date = :date")
    fun selectVeterinaryReports(date: String, petId: Long): List<Veterinary>?

    // 처방약물목록 조회
    @Query("select * from vet_medicine where vet_id = :vetId")
    fun selectVMedicineList(vetId: Long): List<VMedicine>?

    // 이미지 목록 조회
    @Query("select * from vet_images where vet_id = :vetId")
    fun selectVImageList(vetId: Long): List<Images>?
}