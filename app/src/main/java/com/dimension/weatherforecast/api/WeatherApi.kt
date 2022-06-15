package com.dimension.tasty.api

import com.dimension.weatherforecast.models.df.Current
import com.dimension.weatherforecast.models.etc.Forecast
import com.dimension.weatherforecast.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi  {

    @GET("forecast/daily")
    suspend fun getDailyForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("key") apiKey : String
    ): Response<Forecast>

    @GET("current")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("key") apiKey : String
    ): Response<Current>
}