package com.dimension.weatherforecast.repository


import com.dimension.tasty.api.RetrofitInstance
import com.dimension.weatherforecast.db.WeatherDataBase
import com.dimension.weatherforecast.util.Constants.Companion.API_KEY


class WeatherRepository(
    val db: WeatherDataBase
) {
    suspend fun getForecast(latitude: Double, longitude: Double) =
        RetrofitInstance.api.getDailyForecast(latitude,  longitude, API_KEY)


}

