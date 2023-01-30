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


enum class WeatherType(@DrawableRes val icon: Int) {
    SUN(R.drawable.ic_sun),
    SUNNY(R.drawable.ic_sunny),
    RAIN(R.drawable.ic_rain),
    CLOUD(R.drawable.ic_cloud),
    SNOW(R.drawable.ic_snow),
    UNKNOWN(0)
}