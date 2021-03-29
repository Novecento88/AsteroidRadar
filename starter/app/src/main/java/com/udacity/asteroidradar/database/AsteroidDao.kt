package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroidEntities: List<AsteroidEntity>)

    @Transaction
    fun updateAsteroidList(asteroidEntities: List<AsteroidEntity>) {
        deleteAll()
        insertAll(asteroidEntities)
    }

    @Query("DELETE  FROM asteroid_table")
    fun deleteAll()

    @Query("SELECT * FROM asteroid_table order by close_approach_date ASC")
    fun getAsteroids() : LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid_table where code_name = :codeName")
    fun getAsteroid(codeName: String) : AsteroidEntity

    @Query("SELECT * FROM asteroid_table where close_approach_date = :date order by close_approach_date ASC")
    fun getAsteroidsByDate(date: String) : LiveData<List<AsteroidEntity>>
}