package eu.yeger.koffee.ui

class SimpleAction : ResettableLiveData<Boolean>(false) {
    fun activate() = postValue(true)
    override fun reset() = postValue(false)
}
