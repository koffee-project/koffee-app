package eu.yeger.koffee.utility

import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Utility function for creating anonymous ViewModelFactories. Inspired by https://proandroiddev.com/kotlin-delegates-in-android-development-part-2-2c15c11ff438
 *
 * @param VM Type of the ViewModel.
 * @param provider The provider of the ViewModel.
 * @return The lazy ViewModel delegate
 */
inline fun <reified VM : ViewModel> Fragment.viewModelFactories(
    crossinline provider: () -> VM
): Lazy<VM> {
    val factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return provider() as T
            }
        }
    }
    return viewModels(factoryProducer = factoryProducer)
}

fun singleTickTimer(duration: Long, onFinish: () -> Unit) = object : CountDownTimer(duration, duration) {
    override fun onFinish() = onFinish()
    override fun onTick(millisUntilFinished: Long) = Unit
}
