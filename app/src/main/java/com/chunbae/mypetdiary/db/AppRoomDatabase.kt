package com.chunbae.mypetdiary.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chunbae.mypetdiary.common.DateConverter
import com.chunbae.mypetdiary.db.dao.GuardianDao
import com.chunbae.mypetdiary.db.dao.PetDao
import com.chunbae.mypetdiary.db.domain.pet.Pet
import com.chunbae.mypetdiary.db.domain.user.Guardian
import com.chunbae.mypetdiary.db.domain.write.Medicine
import com.chunbae.mypetdiary.db.domain.write.Veterinary
//, Pet::class, Medicine::class, Veterinary::class
@Database(entities = arrayOf(Guardian::class, Pet::class), version = 1)
@TypeConverters(
    value = [
        DateConverter::class,
    ]
)
abstract class AppRoomDatabase : RoomDatabase() {

    //DAO
    abstract fun getGuardianDao(): GuardianDao
    abstract fun getPetDao(): PetDao

    companion object {
        val databaseName = "MyPetDiary"
        var appDatabase: AppRoomDatabase? = null

        fun getInstance(context: Context) : AppRoomDatabase? {
            if(appDatabase == null){
                appDatabase = Room.databaseBuilder(context,
                    AppRoomDatabase::class.java,
                    databaseName
                    )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return appDatabase
        }

        fun getDatabase(context: Context): AppRoomDatabase {
            if(appDatabase == null){
                getInstance(context)
            }
            return appDatabase!!
        }
    }
}