package com.dimension.weatherforecast.db

import android.content.Context
import androidx.room.Database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dimension.weatherforecast.models.City
import java.util.concurrent.Executors


@Database(
    entities = [City::class],
    version = 1
)




abstract class WeatherDataBase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao



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
            ).addCallback(object : Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadScheduledExecutor().execute(object : Runnable{
                        override fun run() {
                            // first data
                            instance?.getWeatherDao()?.insertAll(listOf(City("Bishkek", 42.882,74.582),
                                City("San Fransisco", 37.7562,-122.443),
                                City("Tokyo", 35.6897,139.692),
                                City("Mecca", 21.422,39.826)))
                        }
                    })
                }
            }).build()
    }
}