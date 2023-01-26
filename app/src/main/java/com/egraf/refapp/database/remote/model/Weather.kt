package com.egraf.refapp.database.local.entities

data class Weather(val date: GameDateTime, val type: WeatherType)

enum class WeatherType { SUN, CLOUDS, RAIN, SNOW }