package eu.yeger.koffee

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import eu.yeger.koffee.worker.CleanupWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val WORKER_TAG = "KOFFEE_WORKER"

/**
 * This app's application.
 * Enables dark mode, configures logging and schedules background work.
 *
 * @author Jan Müller
 */
class KoffeeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        scheduleBackgroundWork()
    }

    private fun scheduleBackgroundWork() {
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.cancelAllWorkByTag(WORKER_TAG)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(true)
            .setRequiresStorageNotLow(false)
            .build()

        val workRequest = PeriodicWorkRequest.Builder(
            CleanupWorker::class.java,
            24L,
            TimeUnit.HOURS
        ).addTag(WORKER_TAG).setConstraints(constraints).build()

        workManager.enqueue(workRequest)
    }
}
