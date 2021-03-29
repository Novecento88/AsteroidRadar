package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.repository.NasaRepository
import retrofit2.HttpException

class RefreshAsteroidsWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val database = NasaDatabase.getDatabase(applicationContext)
        val repository = NasaRepository(database)

        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshAsteroidsWorker"
    }
    
}