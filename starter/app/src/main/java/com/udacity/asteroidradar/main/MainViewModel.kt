package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.AsteroidFilter
import com.udacity.asteroidradar.api.getTodayFormattedDate
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.NasaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(dao: AsteroidDao, application: Application, private val repository: NasaRepository) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    val selectedFilter = MutableLiveData<AsteroidFilter>()

    val asteroids : LiveData<List<Asteroid>> = selectedFilter.switchMap { filter ->
        repository.getAsteroidsSelection(filter)
    }
    val status = repository.status

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()

    val navigateToSelectedAsteroid: MutableLiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    init {
        selectedFilter.value = AsteroidFilter.SHOW_ALL
        viewModelScope.launch {
            repository.refreshAsteroids()
            _pictureOfTheDay.value = repository.getPictureOfTheDay()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}