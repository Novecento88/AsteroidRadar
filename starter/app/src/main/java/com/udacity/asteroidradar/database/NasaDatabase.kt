package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidEntity::class], version = 1, exportSchema = false)
abstract class NasaDatabase : RoomDatabase() {

    abstract val asteroidDao : AsteroidDao

    companion object {

        @Volatile
        private var INSTANCE: NasaDatabase? = null

        fun getDatabase(context: Context): NasaDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            NasaDatabase::class.java,
                            "nasa_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}