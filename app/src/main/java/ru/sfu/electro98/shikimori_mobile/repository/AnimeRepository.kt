package ru.sfu.electro98.shikimori_mobile.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.sfu.electro98.shikimori_mobile.dao.AnimeDatabaseDao
import ru.sfu.electro98.shikimori_mobile.entities.Anime
import ru.sfu.electro98.shikimori_mobile.entities.AnimePreview
import ru.sfu.electro98.shikimori_mobile.entities.AnimeStatus
import ru.sfu.electro98.shikimori_mobile.shiki_api.RestClient

class AnimeRepository(private val animeDatabaseDao: AnimeDatabaseDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun addAnime(anime: Anime) {
        coroutineScope.launch(Dispatchers.IO) {
            animeDatabaseDao.insert(anime)
        }
    }
    fun updateAnime(anime: Anime) {
        coroutineScope.launch(Dispatchers.IO) {
            animeDatabaseDao.update(anime)
        }
    }
    fun deleteAnime(anime: Anime) {
        coroutineScope.launch(Dispatchers.IO) {
            animeDatabaseDao.delete(anime)
        }
    }
    fun getById(id: Int): LiveData<Anime> {
        val foundAnime = MutableLiveData<Anime>()
        coroutineScope.launch(Dispatchers.IO) {
            val anime = animeDatabaseDao.getById(id)
            println("Database $anime")
            if (anime != null) {
                foundAnime.postValue(anime)
                return@launch
            }
            val apiResponse = RestClient().fetchAnimeInfo(id)
            if (apiResponse != null)
                addAnime(apiResponse)
            println("Api response = $apiResponse")
            foundAnime.postValue(apiResponse)
        }
        return foundAnime
    }

    fun getAnimes(
        page: Int? = null,
        limit: Int? = null,
        order: String? = null,
        kind: String? = null,
        status: AnimeStatus? = null,
        season: String? = null,
        score: Float? = null,
        duration: String? = null,
        rating: String? = null,
        genre: List<String>? = null,
        studio: List<String>? = null,
        franchise: List<String>? = null,
        censored: Boolean = true,
        myList: String? = null,
        ids: List<Int>? = null,
        excludeIds: List<Int>? = null,
        search: String? = null,
    ): LiveData<List<AnimePreview>> {
        val animes = MutableLiveData<List<AnimePreview>>()
        coroutineScope.launch(Dispatchers.IO) {
            val apiResponse = RestClient().fetchAnimes(page, limit, order, kind, status, season, score, duration, rating, genre, studio, franchise, censored, myList, ids, excludeIds, search)
                ?: return@launch
//            println("Api response = $apiResponse")
            animes.postValue(apiResponse)
        }
        return animes
    }
}