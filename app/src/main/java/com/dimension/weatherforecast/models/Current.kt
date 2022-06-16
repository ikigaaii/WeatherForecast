package com.dimension.weatherforecast.models

import java.io.Serializable

data class Current(
    val count: Int,
    val `data`: List<CurrentData>
) : Serializable