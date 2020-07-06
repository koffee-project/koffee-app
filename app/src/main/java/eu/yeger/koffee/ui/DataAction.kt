package eu.yeger.koffee.ui

/**
 * Class for [Action]s that use generic data.
 *
 * @param T The type of the data.
 * @constructor
 * Initializes the action with an initial value.
 *
 * @param initialValue The initial value. Defaults to null.
 *
 * @author Jan Müller
 */
class DataAction<T>(initialValue: T? = null) : Action<T?>(initialValue) {

    /**
     * Eventually activates this [DataAction] with the given value.
     *
     * @param value The value to be used.
     */
    fun activateWith(value: T?) = postValue(value)

    /**
     * Completes this [DataAction] by eventually setting its value to null.
     */
    override fun complete() = postValue(null)
}
