package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.PictureOfDay
import retrofit2.http.GET
import retrofit2.http.QueryMap

public interface IRetrofitService {

    @GET("neo/rest/v1/feed/")
    suspend fun asteroidList(@QueryMap queryMap: Map<String, String>) : String

    @GET("planetary/apod")
    suspend fun imageOfTheDay(@QueryMap queryMap: Map<String, String>) : PictureOfDay
}