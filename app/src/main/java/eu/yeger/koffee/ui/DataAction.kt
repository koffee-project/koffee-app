package eu.yeger.koffee.ui

class DataAction<T>(initialValue: T? = null) : Action<T?>(initialValue) {
    fun activateWith(value: T?) = postValue(value)
    override fun complete() = postValue(null)
}
