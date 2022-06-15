package com.dimension.weatherforecast.models

data class CurrentData(
    val app_temp: Double,
    val aqi: Int,
    val city_name: String,
    val clouds: Int,
    val dewpt: Double,
    val lat: Double,
    val lon: Double,
    val pres: Double,
    val rh: Double,
    val ob_time: String,
    val slp: Double,
    val temp: Double,
    val ts: Int,
    val uv: Double,
    val vis: Int,
    val weather: Weather,
    val wind_spd: Double
)