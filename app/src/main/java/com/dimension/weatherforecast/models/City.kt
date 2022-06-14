package com.dimension.weatherforecast.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cities")
data class City(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val ingredients: String,
    val healthScore: Int,
    val cookingTime: Int,
    val instruction: String
) : Serializable