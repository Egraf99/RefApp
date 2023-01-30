package com.egraf.refapp.database.remote.model.open_weather_pojo

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DateTimeWeather>,
    val message: Int
)