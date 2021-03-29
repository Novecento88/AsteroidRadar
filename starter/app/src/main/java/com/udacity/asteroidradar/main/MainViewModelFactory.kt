package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.repository.NasaRepository

class MainViewModelFactory(
        private val dao: AsteroidDao,
        private val application: Application,
        private val repository: NasaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dao, application, repository) as T
        }
        throw IllegalArgumentException("Unable to construct ViewModel")
    }

}