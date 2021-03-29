package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class NasaRepository(private val database: NasaDatabase) {

    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()){asteroids ->
        asteroids.asDomainModel()
    }

    fun getAsteroidsSelection(filter: AsteroidFilter) : LiveData<List<Asteroid>> {
        when (filter) {
            AsteroidFilter.SHOW_TODAY -> {
                return Transformations.map(database.asteroidDao.getAsteroidsByDate(getTodayFormattedDate())) { asteroids ->
                    asteroids.asDomainModel()
                }
            }
            AsteroidFilter.SHOW_WEEK -> {
                return Transformations.map(database.asteroidDao.getAsteroidsByDate(getNextSeventhDayFormattedDate())) { asteroids ->
                    asteroids.asDomainModel()
                }
            }
            AsteroidFilter.SHOW_ALL -> {
                return Transformations.map(database.asteroidDao.getAsteroids()) { asteroids ->
                    asteroids.asDomainModel()
                }
            }
        }
    }


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO){
            val asteroidsDeferred = NasaApi.retrofitService.getAsteroidList("", getNextSeventhDayFormattedDate())

            try {
                _status.postValue(NasaApiStatus.LOADING)
                _status.postValue(NasaApiStatus.DONE)
                database.asteroidDao.updateAsteroidList(parseAsteroidsJsonResult(JSONObject(asteroidsDeferred)))
            } catch (e: Exception) {
                _status.postValue(NasaApiStatus.ERROR)
                e.printStackTrace()
            }


        }
    }

    suspend fun getPictureOfTheDay() : PictureOfDay? {
        return try {
            NasaApi.retrofitMoshiService.getPictureOfTheDay()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

