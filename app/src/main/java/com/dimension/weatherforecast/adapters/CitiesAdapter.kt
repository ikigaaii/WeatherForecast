package com.dimension.weatherforecast.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dimension.weatherforecast.databinding.CityItemBinding

import com.dimension.weatherforecast.models.City


class CitiesAdapter : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: CityItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesAdapter.ViewHolder {
        val binding = CityItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitiesAdapter.ViewHolder, position: Int) {
        val city = differ.currentList[position]
        holder.binding.apply {
            tvCityText.text = city.name
            holder.itemView.setOnClickListener{
                onCityItemClickListener?.let { it(city) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onCityItemClickListener: ((City) -> Unit)? =  null
    fun setOnCityItemClickListener(listener: (City) -> Unit) {
        onCityItemClickListener = listener
    }


}