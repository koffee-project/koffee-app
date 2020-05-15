package eu.yeger.koffee.utility

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import eu.yeger.koffee.ui.Action

/**
 * Creates a [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that is updated every time one of the [sources] changes.
 *
 * @param T The type of the [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData).
 * @param sources The sources of the returned [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData).
 * @param block Called every time a source changes. Its result is only applied if it differs from the current value.
 * @return The initialized [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData).
 *
 * @author Jan Müller
 */
fun <T> sourcedLiveData(vararg sources: LiveData<*>, block: () -> T?): LiveData<T> =
    MediatorLiveData<T>().apply {
        val onChange = {
            val oldValue = value
            val newValue = block()
            if (oldValue != newValue) value = block()
        }
        onChange()
        sources.forEach { source ->
            addSource(source) { onChange() }
        }
    }

/**
 * Shortcut function for creating [MediatorLiveData](https://developer.android.com/reference/androidx/lifecycle/MediatorLiveData).
 *
 * @param T The type of the [MediatorLiveData](https://developer.android.com/reference/androidx/lifecycle/MediatorLiveData).
 * @param block Applied to the returned [MediatorLiveData](https://developer.android.com/reference/androidx/lifecycle/MediatorLiveData).
 * @return The modified [MediatorLiveData](https://developer.android.com/reference/androidx/lifecycle/MediatorLiveData).
 *
 * @author Jan Müller
 */
inline fun <T> mediatedLiveData(block: MediatorLiveData<T>.() -> Unit): MediatorLiveData<T> =
    MediatorLiveData<T>().apply(block)

fun <T> Fragment.observe(source: LiveData<T>, block: (T) -> Unit) {
    source.observe(viewLifecycleOwner, Observer(block))
}

fun <T> Fragment.observeAction(source: Action<T?>, block: (T) -> Unit) {
    observe(source) {
        it?.let {
            block(it)
            source.complete()
        }
    }
}

fun Fragment.observeAction(source: Action<Boolean?>, block: () -> Unit) {
    observe(source) {
        if (it == true) {
            block()
            source.complete()
        }
    }
}
