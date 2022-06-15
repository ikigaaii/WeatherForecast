package com.dimension.weatherforecast.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dimension.weatherforecast.databinding.ForecastItemBinding
import com.dimension.weatherforecast.models.ForecastData
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter() : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<ForecastData>() {
        override fun areItemsTheSame(oldItem: ForecastData, newItem: ForecastData): Boolean {
            return oldItem.ts == newItem.ts
        }

        override fun areContentsTheSame(oldItem: ForecastData, newItem: ForecastData): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapter.ViewHolder {
        val binding = ForecastItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ForecastAdapter.ViewHolder, position: Int) {
        val forecast = differ.currentList[position]
        holder.binding.apply {


            when(position){
                0 -> tvWeekDay.text = "Today"
                1 -> tvWeekDay.text = "Tomorrow"
                else ->{
                    tvWeekDay.text = getDayOfWeek(forecast.ts)
                }
            }

            tvDate.text = getDate(forecast.ts)
            tvDayTemp.text = "${forecast.high_temp.toInt()}°C"
            tvNightTemp.text = "${forecast.low_temp.toInt()}°C"
            tvRainChance.text = "${forecast.pop}%"
            tvDescription.text = forecast.weather.description

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getDayOfWeek(number: Int): String {
        val date = Date(number.toLong() * 1000)
        val cal: Calendar = Calendar.getInstance()
        cal.setTime(date)

        return when(cal.get(Calendar.DAY_OF_WEEK)){
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5-> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "N/A"

        }
    }

    private fun getDate(s: Int): String? {
        try {
            val sdf = SimpleDateFormat("dd/MM")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }


}