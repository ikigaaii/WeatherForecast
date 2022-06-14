package com.dimension.weatherforecast.models.etc

data class Forecast(
    val dailyList: List<Data>,
    val lat: Double,
    val lon: Double,
)