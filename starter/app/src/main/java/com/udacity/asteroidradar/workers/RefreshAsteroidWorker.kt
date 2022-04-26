package com.udacity.asteroidradar.workers

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repositories.AsteroidRepository
import java.time.LocalDateTime

class RefreshAsteroidWorker(private val context: Context, workerParameters: WorkerParameters): CoroutineWorker(context, workerParameters) {

    companion object {
        const val REFRESH_WORK_NAME = "RefreshAstroidWorker"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        try {
            val repository = AsteroidRepository(context)
            repository.refreshAsteroidInfo()
            repository.deleteAsteroids(LocalDateTime.now())
            return Result.success()
        }
        catch (ex: Exception){

        }
        return Result.retry()
    }
}