package com.dimension.weatherforecast.models.df

data class Current(
    val count: Int,
    val `data`: List<CurrentData>
)