package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidType
import com.udacity.asteroidradar.repositories.AsteroidRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AsteroidRepository(application.applicationContext)
    private var asteroidFilter = MutableLiveData<AsteroidType>(AsteroidType.NEXTWEEK)
    private var _pictureOfTheDayUrl = MutableLiveData<String>()


    @RequiresApi(Build.VERSION_CODES.O)
    private var _asteroidList: LiveData<MutableList<Asteroid>> = Transformations.switchMap(asteroidFilter){
        when(it){
            AsteroidType.NEXTWEEK -> {
                val endDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
                val startDate = LocalDateTime.now().minusDays(7).format(DateTimeFormatter.ISO_DATE)
                repository.getAsteroidsDate(startDate,endDate)
            }
            AsteroidType.TODAY -> {
                val startDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
                repository.getAsteroidsDay(startDate)
            }
            AsteroidType.SAVED -> {
                repository.getAsteroids()
            }

            //else -> throw IllegalStateException()
        }
    }

    val asteroidList: LiveData<MutableList<Asteroid>>
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            return _asteroidList
        }
    val pictureOfTheDayUrl: LiveData<String>
        get() {
            return _pictureOfTheDayUrl
        }

    init {
        var query = HashMap<String, String>()

        viewModelScope.launch {
            repository.refreshAsteroidInfo()
        }

        viewModelScope.launch {
            var result = repository.getImageOfTheDay()
            _pictureOfTheDayUrl.value = result.url
        }

    }

    fun setFilter(asteroidType: AsteroidType){
        when(asteroidType){
            AsteroidType.NEXTWEEK -> {
                asteroidFilter.value = asteroidType
            }
            AsteroidType.TODAY -> {
                asteroidFilter.value = asteroidType
            }
            AsteroidType.SAVED -> {
                asteroidFilter.value = asteroidType
            }

        }
    }
}