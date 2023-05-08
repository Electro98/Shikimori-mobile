package ru.sfu.electro98.shikimori_mobile.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.sfu.electro98.shikimori_mobile.entities.Anime

@Dao
interface AnimeDatabaseDao {
    @Query("SELECT * from anime_base where id = :id")
    fun getById(id: Int): Anime

    @Insert
    suspend fun insert(item: Anime)

    @Delete
    suspend fun delete(item: Anime)
}