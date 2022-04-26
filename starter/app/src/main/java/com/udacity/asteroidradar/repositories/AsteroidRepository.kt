package com.udacity.asteroidradar.repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.RetrofitService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AsteroidRepository(private val context: Context) {

    suspend fun getImageOfTheDay(): PictureOfDay {

        var query = HashMap<String, String>()
        query["api_key"] = Constants.API_KEY
        var retrofit = RetrofitService.getRetrofitService()

        return retrofit.imageOfTheDay(query)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deleteAsteroids(date: LocalDateTime){
        withContext(Dispatchers.IO) {
            val database = AsteroidDatabase.getInstance(context)
            database.asteroidDatabase().deleteAsteroidsByDate(date.format(DateTimeFormatter.ISO_DATE))
        }
    }

    suspend fun refreshAsteroidInfo(){
        withContext(Dispatchers.IO) {
            var query = HashMap<String, String>()
            query["api_key"] = Constants.API_KEY
            var retrofit = RetrofitService.getRetrofitService()
            var response = retrofit.asteroidList(query)

            var result = parseAsteroidsJsonResult(JSONObject(response))
            val database = AsteroidDatabase.getInstance(context)
            database.asteroidDatabase().insertAll(convertToAsteriodsEntity(result))
        }
    }

    fun getAsteroidsDate(startDate: String, endDate: String): LiveData<MutableList<Asteroid>>{
        val database = AsteroidDatabase.getInstance(context)
        val response = database.asteroidDatabase().getAsteroidsDate(startDate, endDate)
        return Transformations.map(response){convertToAsteriods(it)}
    }

    fun getAsteroidsDay(startDate: String): LiveData<MutableList<Asteroid>>{
        val database = AsteroidDatabase.getInstance(context)
        val response = database.asteroidDatabase().getAsteroidsDay(startDate)
        return Transformations.map(response){convertToAsteriods(it)}
    }

    fun getAsteroids(): LiveData<MutableList<Asteroid>>{
        val database = AsteroidDatabase.getInstance(context)
        val response = database.asteroidDatabase().getAsteroids()
        return Transformations.map(response){convertToAsteriods(it)}
    }

    fun convertToAsteriods(asteroidEntities: List<AsteroidEntity>): MutableList<Asteroid>{
        val result = mutableListOf<Asteroid>()
        for (item in asteroidEntities){
            result.add(Asteroid(item.id, item.codename, item.closeApproachDate, item.absoluteMagnitude, item.estimatedDiameter,
                item.relativeVelocity, item.distanceFromEarth, item.isPotentiallyHazardous))
        }

        return result
    }

    fun convertToAsteriodsEntity(asteroids: List<Asteroid>): MutableList<AsteroidEntity>{
        val result = mutableListOf<AsteroidEntity>()
        for (item in asteroids){
            result.add(
                AsteroidEntity(item.id, item.codename, item.closeApproachDate, item.absoluteMagnitude, item.estimatedDiameter,
                item.relativeVelocity, item.distanceFromEarth, item.isPotentiallyHazardous)
            )
        }

        return result
    }
}