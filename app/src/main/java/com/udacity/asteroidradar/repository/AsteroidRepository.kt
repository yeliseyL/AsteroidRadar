package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.NASAApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.UnknownHostException

class AsteroidRepository(private val database: AsteroidDatabase) {

    private val startDate = getNextSevenDaysFormattedDates()[0]
    private val endDate = getNextSevenDaysFormattedDates()[7]

    var asteroids: MutableLiveData<ArrayList<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids(startDate, endDate)) {
            it.asDomainModel()
        } as MutableLiveData<ArrayList<Asteroid>>

    suspend fun getTodayAsteroids() {
        withContext(Dispatchers.IO) {
            asteroids.value?.clear()
            val todayAsteroids = database.asteroidDao.getTodayAsteroids(startDate).asDomainModel()
            asteroids.value?.addAll(todayAsteroids)
        }
    }

    suspend fun getAllAsteroids() {
        withContext(Dispatchers.IO) {
            asteroids.value?.clear()
            val allAsteroids = database.asteroidDao.getAllAsteroids().asDomainModel()
            asteroids.value?.addAll(allAsteroids)
        }
    }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            try {
                val response = NASAApi.retrofitAsteroidsService.getAsteroids(startDate, endDate,
                    BuildConfig.API_KEY)
                val jsonObject = JSONObject(response)
                val asteroids = parseAsteroidsJsonResult(jsonObject)
                database.asteroidDao.insertAll(asteroids.asDatabaseModel())
            } catch (ex: UnknownHostException) {
                Log.e("AsteroidRepository", "no network error")
            }
        }
    }
}