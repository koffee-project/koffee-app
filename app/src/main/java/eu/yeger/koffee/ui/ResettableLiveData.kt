package eu.yeger.koffee.ui

import androidx.lifecycle.LiveData

abstract class ResettableLiveData<T>(initialValue: T? = null) : LiveData<T>(initialValue) {
    abstract fun reset()
}
