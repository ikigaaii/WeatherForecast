package com.dimension.tasty.api

import com.dimension.weatherforecast.models.City
import com.dimension.weatherforecast.models.CityList
import com.dimension.weatherforecast.models.Current
import com.dimension.weatherforecast.models.Forecast
import com.dimension.weatherforecast.util.Constants.Companion.CITY_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

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

    //search city api
    @Headers("X-Api-Key: " + CITY_API_KEY)
    @GET
    suspend fun searchCities(
        @Url url: String
    ): Response<CityList>
}