package com.dimension.weatherforecast.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cities")
data class City(

    @PrimaryKey val name: String,
    val latitude: Double,
    val longitude: Double
) : Serializable{
    fun populateData() : List<City> {
        return listOf(City("Bishkek", 42.882,74.582),
            City("New York", 40.730,-73.935),
            City("Melbourne", -37.815,144.946),
            City("Mecca", 21.422,39.826))
    }
}