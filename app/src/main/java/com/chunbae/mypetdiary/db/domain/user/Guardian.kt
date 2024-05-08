package com.chunbae.mypetdiary.db.domain.user

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "guardian")
data class Guardian (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "guardian_id") val id: Long?,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "password")  val password: String,
    @ColumnInfo(name = "user_id")  val userId: String
){
    @Ignore
    constructor(
        userId: String,
        nickname: String,
        password: String
    ): this(
        id = null,
        userId = userId,
        nickname = nickname,
        password = password
    )
}