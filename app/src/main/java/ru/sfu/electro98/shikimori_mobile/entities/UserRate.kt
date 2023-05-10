package ru.sfu.electro98.shikimori_mobile.entities

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "user_rates",
    indices = [Index(value = ["target_id"], unique = true)]
)
data class UserRate(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("target_id")
    @ColumnInfo(name = "target_id")
    val targetId: Int,
    val name: String,
    @SerializedName("russian")
    @ColumnInfo(name = "russian")
    val russianName: String,
    var status: RateStatus,
    var rate: Float,
)

enum class RateStatus {
    planned, watching, rewatching, completed, on_hold, dropped;
    companion object {
        private val names: Map<RateStatus, String> = mapOf(
            Pair(planned, "Planned"),
            Pair(watching, "Watching"),
            Pair(rewatching, "Re-Watching"),
            Pair(completed, "Completed"),
            Pair(on_hold, "On hold"),
            Pair(dropped, "Dropped"),
        )
        private val russiansNames: Map<RateStatus, String> = mapOf(
            Pair(planned, "Запланировано"),
            Pair(watching, "Смотрю"),
            Pair(rewatching, "Пересматриваю"),
            Pair(completed, "Просмотрено"),
            Pair(on_hold, "Отложено"),
            Pair(dropped, "Брошено"),
        )

        fun getName(rateStatus: RateStatus): String {
            return names.getOrDefault(rateStatus, "???")
        }
        fun getRussianName(rateStatus: RateStatus): String {
            return russiansNames.getOrDefault(rateStatus, "???")
        }
    }
}
