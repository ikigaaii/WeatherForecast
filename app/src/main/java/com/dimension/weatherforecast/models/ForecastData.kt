package com.dimension.weatherforecast.models

data class ForecastData(
    val app_max_temp: Double,
    val app_min_temp: Double,
    val clouds: Int,
    val datetime: String,
    val dewpt: Double,
    val high_temp: Double,
    val low_temp: Double,
    val moonrise_ts: Int,
    val moonset_ts: Int,
    val pop: Int,
    val precip: Double,
    val pres: Double,
    val rh: Int,
    val aqi: Int,
    val sunrise_ts: Int,
    val sunset_ts: Int,
    val temp: Double,
    val ts: Int,
    val valid_date: String,
    val vis: Double,
    val weather: Weather,
    val wind_spd: Double
)