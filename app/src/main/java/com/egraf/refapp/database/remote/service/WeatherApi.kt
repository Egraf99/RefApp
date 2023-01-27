package com.egraf.refapp.database.remote.service

import com.egraf.refapp.database.remote.model.WeatherResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast")
    fun getWeatherForecast(
        @Query("appid") appKey: String = "dbb10380ae00ec1cb1ec4e944cb41d4e",
        @Query("q") city: String = "Москва,ru",
        @Query("units") units: String = "metric",
    ): Call<WeatherResponse>
}