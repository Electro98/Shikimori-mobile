package ru.sfu.electro98.shikimori_mobile.entities

import androidx.room.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName


// use Gson library to convert json into data class
// https://www.reddit.com/r/androiddev/comments/s8ohcy/how_can_i_store_nested_json_data_in_room_database/
@Entity("anime_base")
data class Anime(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val name: String,
    @SerializedName("russian")
    @ColumnInfo(name = "russian")
    val russianName: String,

    @Embedded(prefix = "img_")
    val image: AnimeImage,

    val url: String,
    val kind: String,
    val score: Float,
    val status: AnimeStatus,
    val episodes: Int,
    @SerializedName("episodes_aired")
    @ColumnInfo(name = "episodes_aired")
    val episodesAired: Int,
    @SerializedName("aired_on")
    @ColumnInfo(name = "aired_on")
    val airedOn: String?, // TODO: redefine type
    @SerializedName("released_on")
    @ColumnInfo(name = "released_on")
    val releasedOn: String?, // TODO: redefine type
    val rating: String,

    @SerializedName("english")
    @ColumnInfo(name = "english")
    val englishNames: List<String>,
    @SerializedName("japanese")
    @ColumnInfo(name = "japanese")
    val japaneseNames: List<String>,
    val synonyms: List<String>,

    @SerializedName("license_name_ru")
    @ColumnInfo(name = "license_name_ru")
    val licenseNameRU: String?,
    val duration: Int,
    val description: String?,
    @SerializedName("description_source")
    @ColumnInfo(name = "description_source")
    val descriptionSource: String?,
    val franchise: String?,
    val favoured: Boolean,
    val anons: Boolean,
    val ongoing: Boolean,
    @SerializedName("thread_id")
    @ColumnInfo(name = "thread_id")
    val threadId: Int,
    @SerializedName("topic_id")
    @ColumnInfo(name = "topic_id")
    val topicId: Int,
    @SerializedName("myanimelist_id")
    @ColumnInfo(name = "myanimelist_id")
    val idMAL: Int,

    // rates_scores_stats
    // rates_statuses_stats

    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    val updatedAt: String?,
    @SerializedName("next_episode_at")
    @ColumnInfo(name = "next_episode_at")
    val nextEpisodeAt: String?,  // TODO: redefine type

    val fansubbers: List<String>,
    val fandubbers: List<String>,
    val licensors: List<String>,

    // genres
    // studious
    // videos
    // screenshots

    //val user_rate, TODO: define type
) {
    @get:Ignore
    val episodesTrulyAired: Int
        get() = if (status == AnimeStatus.released) episodes else episodesAired

    fun createUserRate(status: RateStatus): UserRate {
        return UserRate(
            id = 0,
            targetId = id,
            name = name,
            russianName = russianName,
            status = status,
            rate = 0f,
        )
    }
}


data class AnimeImage(
    val original: String,
    val preview: String,
    val x96: String,
    val x48: String,
)


enum class AnimeStatus { anons, ongoing, released }


data class AnimePreview(
    val id: Int,
    val name: String,
    @SerializedName("russian")
    val russianName: String,
    val image: AnimeImage,
    val url: String,
    val kind: String,
    val score: Float,
    val status: AnimeStatus,
    val episodes: Int,
    @SerializedName("episodes_aired")
    val episodesAired: Int,
    @SerializedName("aired_on")
    val airedOn: String?, // TODO: redefine type
    @SerializedName("released_on")
    val releasedOn: String?, // TODO: redefine type
)
