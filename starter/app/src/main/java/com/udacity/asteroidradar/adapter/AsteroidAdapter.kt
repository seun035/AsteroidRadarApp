package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidDiffCallback
import com.udacity.asteroidradar.AsteroidItemListener
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

class AsteroidAdapter(val asteroidItemListener: AsteroidItemListener) : ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(AsteroidDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        var asteroidListBinding = AsteroidListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AsteroidViewHolder(asteroidListBinding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(asteroidItemListener, getItem(position))
    }

    class AsteroidViewHolder(var binding: AsteroidListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(asteroidItemListener: AsteroidItemListener, asteroid: Asteroid){
            binding.asteroid = asteroid
            binding.asteroidItemlistener = asteroidItemListener
            binding.executePendingBindings()
        }
    }

}