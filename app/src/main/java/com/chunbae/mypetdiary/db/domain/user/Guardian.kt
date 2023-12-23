package com.chunbae.mypetdiary.db.domain.user

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guardian")
data class Guardian (
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "nickname") var nickname: String,
    @ColumnInfo(name = "password") @Nullable var password: String?,
    @ColumnInfo(name = "user_id") @Nullable var userId: String?
) {


}