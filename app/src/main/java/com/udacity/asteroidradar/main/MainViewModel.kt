package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.NASAApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

enum class NasaApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    init {
        getPictureOfDay()
        getAsteroids()
    }

    val asteroids = asteroidRepository.asteroids

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = NASAApi.retrofitPictureService.getPictureOfTheDay(BuildConfig.API_KEY)
            } catch (ex: UnknownHostException) {
                Log.e("AsteroidRepository", "no network error")
            }
        }
    }

    fun getAsteroids() {
        viewModelScope.launch {
            try {
                _status.value = NasaApiStatus.LOADING
                asteroidRepository.refreshVideos()
                _status.value = NasaApiStatus.DONE
            } catch (ex: UnknownHostException) {
                Log.e("AsteroidRepository", "no network error")
                _status.value = NasaApiStatus.ERROR
            }
        }
    }

    fun getTodayAsteroids() {
        viewModelScope.launch {
            try {
                _status.value = NasaApiStatus.LOADING
                asteroidRepository.getTodayAsteroids()
                _status.value = NasaApiStatus.DONE
            } catch (ex: UnknownHostException) {
                _status.value = NasaApiStatus.ERROR
            }
        }
    }

    fun getAllAsteroids() {
        viewModelScope.launch {
            try {
                _status.value = NasaApiStatus.LOADING
                asteroidRepository.getAllAsteroids()
                _status.value = NasaApiStatus.DONE
            } catch (ex: UnknownHostException) {
                _status.value = NasaApiStatus.ERROR
            }
        }
    }


    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}