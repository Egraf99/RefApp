package com.egraf.refapp.database.remote.model.open_weather_pojo

data class DateTimeWeather(
    val clouds: Clouds,
    val dt: Long,
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val snow: Snow,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather_>,
    val wind: Wind
)