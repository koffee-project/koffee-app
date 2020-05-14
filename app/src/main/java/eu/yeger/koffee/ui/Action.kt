package eu.yeger.koffee.ui

class Action<T>(initialValue: T? = null) : ResettableLiveData<T>(initialValue) {
    fun activate(value: T) = postValue(value)
    override fun reset() = postValue(null)
}
