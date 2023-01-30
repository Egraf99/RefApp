package com.egraf.refapp.database.remote.model

import androidx.annotation.DrawableRes
import com.egraf.refapp.R

data class Weather(
    val temp: Double,
    val type: WeatherType
) {
    companion object {
        operator fun invoke(temp: Double, icon: String): Weather = when (icon) {
            "01d", "01n" -> Weather(temp, WeatherType.SUN)
            "02d", "02n" -> Weather(temp, WeatherType.SUNNY)
            "03d", "03n", "04d", "04n" -> Weather(temp, WeatherType.CLOUD)
            "09d", "09n", "10d", "10n", "11d", "11n" -> Weather(temp, WeatherType.RAIN)
            "13d", "13n" -> Weather(temp, WeatherType.SNOW)
            else -> Weather(temp, WeatherType.UNKNOWN)
        }
    }
}

enum class WeatherType(val icon: Result<Int>) {
    SUN(Result.success(R.drawable.ic_sun)),
    SUNNY(Result.success(R.drawable.ic_sunny)),
    RAIN(Result.success(R.drawable.ic_rain)),
    CLOUD(Result.success(R.drawable.ic_cloud)),
    SNOW(Result.success(R.drawable.ic_snow)),
    UNKNOWN(Result.failure(IllegalStateException("Unknown type")))
}