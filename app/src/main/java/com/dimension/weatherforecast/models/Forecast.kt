package com.dimension.weatherforecast.models

import java.io.Serializable

data class Forecast(
    val `data`: List<ForecastData>,
    val lat: Double,
    val lon: Double,
) : Serializable