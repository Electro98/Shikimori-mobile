package ru.sfu.electro98.shikimori_mobile.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.sfu.electro98.shikimori_mobile.dao.UserRatesDatabaseDao
import ru.sfu.electro98.shikimori_mobile.entities.RateStatus
import ru.sfu.electro98.shikimori_mobile.entities.UserRate

class UserRatesRepository(private val userRatesDatabaseDao: UserRatesDatabaseDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addUserRate(userRate: UserRate) {
        coroutineScope.launch(Dispatchers.IO) {
            userRatesDatabaseDao.insert(userRate)
        }
    }

    fun updateUserRate(userRate: UserRate) {
        coroutineScope.launch(Dispatchers.IO) {
            userRatesDatabaseDao.update(userRate)
        }
    }

    fun deleteUserRate(userRate: UserRate) {
        coroutineScope.launch(Dispatchers.IO) {
            userRatesDatabaseDao.delete(userRate)
        }
    }

    fun getById(id: Int): LiveData<UserRate> {
        val foundUserRate = MutableLiveData<UserRate>()
        coroutineScope.launch(Dispatchers.IO) {
            val dbAnswer = userRatesDatabaseDao.getById(id)
            foundUserRate.postValue(dbAnswer)
        }
        return foundUserRate
    }

    fun getByAnimeId(animeId: Int): Flow<UserRate?> {
        return userRatesDatabaseDao.getByAnimeId(animeId)
    }

    fun getByStatus(ratesStatus: RateStatus): Flow<List<UserRate>> {
        return userRatesDatabaseDao.getByStatus(ratesStatus)
    }

    fun getAllRates(): Flow<List<UserRate>> {
        return userRatesDatabaseDao.getAllRates()
    }
}