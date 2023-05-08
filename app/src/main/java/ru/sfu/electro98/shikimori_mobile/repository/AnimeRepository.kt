package ru.sfu.electro98.shikimori_mobile.repository

import ru.sfu.electro98.shikimori_mobile.dao.AnimeDatabaseDao
import ru.sfu.electro98.shikimori_mobile.entities.Anime

class AnimeRepository(private val animeDatabaseDao: AnimeDatabaseDao) {
    suspend fun addAnime(anime: Anime) {
        animeDatabaseDao.insert(anime)
    }
    suspend fun deleteAnime(anime: Anime) {
        animeDatabaseDao.delete(anime)
    }
    fun getById(id: Int): Anime {
        return animeDatabaseDao.getById(id)
    }
}