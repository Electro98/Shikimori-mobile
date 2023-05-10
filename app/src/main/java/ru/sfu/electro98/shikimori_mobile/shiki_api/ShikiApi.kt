package ru.sfu.electro98.shikimori_mobile.shiki_api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sfu.electro98.shikimori_mobile.entities.Anime
import ru.sfu.electro98.shikimori_mobile.entities.AnimePreview
import ru.sfu.electro98.shikimori_mobile.entities.AnimeStatus

interface ShikiApi {
    @Headers(
        "Accept: application/json",
        "Cache-Control: no-cache"
    )
    @GET("api/animes/{id}")
    fun fetchAnimeInfo(@Path("id") id: Int): Call<Anime>?

    @Headers("Accept: application/json")
    @GET("api/animes")
    fun fetchAnimes(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("order") order: String? = null,
        @Query("kind") kind: String? = null,
        @Query("status") status: AnimeStatus? = null,
        @Query("season") season: String? = null,
        @Query("score") score: Float? = null,
        @Query("duration") duration: String? = null,
        @Query("rating") rating: String? = null,
        @Query("genre") genre: List<String>? = null,
        @Query("studio") studio: List<String>? = null,
        @Query("franchise") franchise: List<String>? = null,
        @Query("censored") censored: Boolean = true,
        @Query("mylist") myList: String? = null,
        @Query("ids") ids: List<Int>? = null,
        @Query("exclude_ids") excludeIds: List<Int>? = null,
        @Query("search") search: String? = null,
    ): Call<List<AnimePreview>>
}