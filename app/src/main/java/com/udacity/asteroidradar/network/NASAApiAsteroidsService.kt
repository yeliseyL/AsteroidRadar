package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.PictureOfDay
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NASAApiAsteroidsService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("start_date") startDate: String,
                                   @Query("end_date") endDate: String,
                                   @Query("api_key") key: String): String
}