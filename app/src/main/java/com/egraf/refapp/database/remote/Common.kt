package com.egraf.refapp.database.remote

import com.egraf.refapp.database.remote.retrofit.RetrofitClient
import com.egraf.refapp.database.remote.service.WeatherApi

object Common {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val weatherService: WeatherApi
        get() = RetrofitClient.getRetrofit(BASE_URL).create(WeatherApi::class.java)
}