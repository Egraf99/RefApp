package com.egraf.refapp.database.remote.model

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DateTimeWeather>,
    val message: Int
)