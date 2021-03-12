package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.PictureOfDay
import retrofit2.http.GET
import retrofit2.http.Query

interface NASAApiPictureService {
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") key: String): PictureOfDay
}