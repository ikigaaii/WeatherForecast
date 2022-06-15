package com.dimension.weatherforecast.models

data class Forecast(
    val `data`: List<ForecastData>,
    val lat: Double,
    val lon: Double,
)