package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.workers.RefreshAsteroidWorker
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        setUpAsteroidRefresh()
    }

    private fun setUpAsteroidRefresh() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true).build()

        val workRequest = PeriodicWorkRequest
            .Builder(RefreshAsteroidWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints).build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(RefreshAsteroidWorker.REFRESH_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest)

    }
}