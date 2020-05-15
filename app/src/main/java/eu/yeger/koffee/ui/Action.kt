package eu.yeger.koffee.ui

import androidx.lifecycle.LiveData

abstract class Action<T>(initialValue: T? = null) : LiveData<T>(initialValue) {
    abstract fun complete()
}
