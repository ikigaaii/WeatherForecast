package com.dimension.weatherforecast.db


import androidx.room.*
import com.dimension.weatherforecast.models.City


@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(city: City): Long

    @Insert
    fun insertAll(city : List<City>)

    @Query("Select * from cities")
    suspend fun getAllCities(): List<City>

    @Query("Select name from cities")
    suspend fun getCitiesName(): List<String>

    @Delete
    suspend fun deleteCity(city: City)


}