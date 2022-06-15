package com.dimension.weatherforecast.models.etc

data class Forecast(
    val `data`: List<ForecastData>,
    val lat: Double,
    val lon: Double,
)