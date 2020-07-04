package eu.yeger.koffee.ui

/**
 * Class for binary [Action]s.
 *
 * @author Jan Müller
 */
class SimpleAction : Action<Boolean?>(false) {

    /**
     * Eventually activates this [SimpleAction] by setting its value to true.
     */
    fun activate() = postValue(true)

    /**
     * Eventually completes this [SimpleAction] by setting its value to false.
     */
    override fun complete() = postValue(false)
}
