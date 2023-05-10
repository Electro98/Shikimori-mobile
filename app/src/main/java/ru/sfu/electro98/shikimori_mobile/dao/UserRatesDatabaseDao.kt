package ru.sfu.electro98.shikimori_mobile.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.sfu.electro98.shikimori_mobile.entities.RateStatus
import ru.sfu.electro98.shikimori_mobile.entities.UserRate

@Dao
interface UserRatesDatabaseDao {
    @Query("SELECT * from user_rates where id = :id")
    fun getById(id: Int): UserRate?

    @Query("SELECT * from user_rates where target_id = :id")
    fun getByAnimeId(id: Int): Flow<UserRate?>

    @Query("SELECT * from user_rates where status = :ratesStatus")
    fun getByStatus(ratesStatus: RateStatus): Flow<List<UserRate>>

    @Query("SELECT * from user_rates")
    fun getAllRates(): Flow<List<UserRate>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: UserRate)

    @Update
    suspend fun update(item: UserRate)

    @Delete
    suspend fun delete(item: UserRate)
}