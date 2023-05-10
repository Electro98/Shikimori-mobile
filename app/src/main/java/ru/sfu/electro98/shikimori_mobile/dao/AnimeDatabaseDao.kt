package ru.sfu.electro98.shikimori_mobile.dao

import androidx.room.*
import ru.sfu.electro98.shikimori_mobile.entities.Anime

@Dao
interface AnimeDatabaseDao {
    @Query("SELECT * from anime_base where id = :id")
    fun getById(id: Int): Anime?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Anime)

    @Update
    suspend fun update(item: Anime)

    @Delete
    suspend fun delete(item: Anime)
}