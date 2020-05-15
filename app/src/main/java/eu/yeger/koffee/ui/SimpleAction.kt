package eu.yeger.koffee.ui

class SimpleAction : Action<Boolean?>(false) {
    fun activate() = postValue(true)
    override fun complete() = postValue(false)
}
