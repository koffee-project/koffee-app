package eu.yeger.koffee

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

class KoffeeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        Timber.plant(Timber.DebugTree())
    }
}
