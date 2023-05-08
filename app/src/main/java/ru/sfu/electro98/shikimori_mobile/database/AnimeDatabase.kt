package ru.sfu.electro98.shikimori_mobile.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.sfu.electro98.shikimori_mobile.dao.AnimeDatabaseDao
import ru.sfu.electro98.shikimori_mobile.entities.Anime

@TypeConverters(value = [Converters::class])
@Database(entities = [Anime::class], version = 1)
abstract class AnimeDatabase: RoomDatabase() {
    abstract fun animeDao(): AnimeDatabaseDao
    companion object {
        private var INSTANCE: AnimeDatabase? = null
        fun getInstance(context: Context): AnimeDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AnimeDatabase::class.java,
                        "anime_database",
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}