package com.dimension.weatherforecast.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dimension.weatherforecast.databinding.ForecastItemBinding
import com.dimension.weatherforecast.models.etc.ForecastData

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {


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

            tvDayTemp.text = "${forecast.high_temp}°C"
            tvNightTemp.text = "${forecast.low_temp}°C"
            tvRainChance.text = "${forecast.pop}%"
            tvDescription.text = forecast.weather.description

        }

    }

    override fun getItemCount(): Int {
        return 3
    }




}