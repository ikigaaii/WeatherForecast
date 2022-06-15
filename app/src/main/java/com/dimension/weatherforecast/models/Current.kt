package com.dimension.weatherforecast.models

data class Current(
    val count: Int,
    val `data`: List<CurrentData>
)