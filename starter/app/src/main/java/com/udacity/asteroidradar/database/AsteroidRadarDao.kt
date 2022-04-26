package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface AsteroidRadarDao {

    @Insert(onConflict = REPLACE)
    fun insertAll(entities: List<AsteroidEntity>)

    @Delete
    fun deleteAll(asteroidEntities: List<AsteroidEntity>)

    @Query("delete from asteroid where closeApproachDate < :date")
    fun deleteAsteroidsByDate(date: String)

    @Query("select * from asteroid where closeApproachDate between :startDate and :endDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsDate(startDate: String, endDate: String):LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid ORDER BY closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid WHERE closeApproachDate = :startDate ORDER BY closeApproachDate DESC")
    fun getAsteroidsDay(startDate: String): LiveData<List<AsteroidEntity>>
}