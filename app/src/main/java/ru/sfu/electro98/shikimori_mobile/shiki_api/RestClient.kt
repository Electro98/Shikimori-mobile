package ru.sfu.electro98.shikimori_mobile.shiki_api

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import ru.sfu.electro98.shikimori_mobile.MainActivity
import ru.sfu.electro98.shikimori_mobile.entities.Anime
import ru.sfu.electro98.shikimori_mobile.entities.AnimePreview
import ru.sfu.electro98.shikimori_mobile.entities.AnimeStatus
import java.util.concurrent.TimeUnit

class RestClient {
    private val diskCacheSize: Long = 10 * 1024 * 1024 // 10MB
    private val service: ShikiApi
    fun getService(): ShikiApi {
        return service
    }
    init {
        val baseURL = "https://shikimori.me/"
        val context = MainActivity.applicationContext()
        val cache = Cache(context.cacheDir, diskCacheSize)
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1,  TimeUnit.SECONDS)
            .writeTimeout(1,  TimeUnit.SECONDS)
            .cache(cache)
        val gson = GsonBuilder()
            .serializeNulls()
//            .setDateFormat()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()
        service = retrofit.create(ShikiApi::class.java)
    }

    suspend fun fetchAnimeInfo(id: Int): Anime? {
        try {
            val call = getService().fetchAnimeInfo(id)
            val response = call?.awaitResponse()
            if (response?.isSuccessful == true) {
                return response.body()
            }
            return null
        } catch (exs: java.lang.Exception) {
            Log.e("RestClient", exs.message?: "", exs)
            return null
        }
    }

    suspend fun fetchAnimes(
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
    ): List<AnimePreview>? {
        try {
            val call = getService().fetchAnimes(page, limit, order, kind, status, season, score, duration, rating, genre, studio, franchise, censored, myList, ids, excludeIds, search)
            val response = call.awaitResponse()
            if (response.isSuccessful) {
                return response.body()
            }
            return listOf()
        } catch (exs: java.lang.Exception) {
            Log.e("RestClient", exs.message?: "", exs)
            return listOf()
        }
    }
}