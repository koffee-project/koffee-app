package eu.yeger.koffee.ui

import androidx.lifecycle.LiveData

/**
 * Special [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) for completable actions.
 *
 * @param T The type of the [Action] value.
 * @constructor
 * Initializes the action with an initial value.
 *
 * @param initialValue The initial value. Defaults to null.
 *
 * @author Jan Müller
 */
abstract class Action<T>(initialValue: T? = null) : LiveData<T>(initialValue) {

    /**
     * Marks this [Action] as completed.
     */
    abstract fun complete()
}
