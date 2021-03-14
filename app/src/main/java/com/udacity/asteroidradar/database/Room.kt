package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroids(startDate: String, endDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate == :startDate")
    fun getTodayAsteroids(startDate: String): List<DatabaseAsteroid>

    @Query("SELECT * FROM asteroid_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): List<DatabaseAsteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<DatabaseAsteroid>)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroids").build()
        }
    }
    return INSTANCE
}
