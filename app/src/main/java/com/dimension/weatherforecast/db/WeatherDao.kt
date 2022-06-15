package com.dimension.weatherforecast.db


import androidx.room.*
import com.dimension.weatherforecast.models.City


@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(city: City): Long

    @Query("Select * from cities")
    suspend fun getAllCities(): List<City>



    @Delete
    suspend fun deleteCity(city: City)


}