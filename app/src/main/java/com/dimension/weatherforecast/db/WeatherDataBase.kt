package com.dimension.weatherforecast.db

import android.content.Context
import androidx.room.Database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.dimension.weatherforecast.models.City


@Database(
    entities = [City::class],
    version = 1
)




abstract class WeatherDataBase : RoomDatabase() {

    abstract fun getMealDao(): WeatherDao



    companion object{
        @Volatile
        private var instance: WeatherDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDataBase(context).also { instance  = it}
        }
        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDataBase::class.java,
                "weather_db.db"
            ).build()
    }
}