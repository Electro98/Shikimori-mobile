package ru.sfu.electro98.shikimori_mobile.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.sfu.electro98.shikimori_mobile.dao.UserRatesDatabaseDao
import ru.sfu.electro98.shikimori_mobile.entities.UserRate

@Database(entities = [UserRate::class], version = 1)
abstract class UserRatesDatabase: RoomDatabase() {
    abstract fun userRatesDao(): UserRatesDatabaseDao

    companion object {
        private var INSTANCE: UserRatesDatabase? = null
        fun getInstance(context: Context): UserRatesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserRatesDatabase::class.java,
                        "user_rates_database",
                    ).fallbackToDestructiveMigration()
//                        .allowMainThreadQueries()
                        .build()
//                    instance = Room.inMemoryDatabaseBuilder(
//                        context.applicationContext,
//                        UserRatesDatabase::class.java,
//                    ).fallbackToDestructiveMigration()
//                        .allowMainThreadQueries()
//                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}