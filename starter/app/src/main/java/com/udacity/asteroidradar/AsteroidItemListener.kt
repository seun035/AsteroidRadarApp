package com.udacity.asteroidradar

class AsteroidItemListener(val listener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = listener(asteroid)
}