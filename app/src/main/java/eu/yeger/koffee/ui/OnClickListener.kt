package eu.yeger.koffee.ui

/**
 * Utility class for type safe click event handling.
 *
 * @param T The input type.
 * @property block Lambda that is run once [onClick] is called.
 *
 * @author Jan Müller
 */
class OnClickListener<T>(private val block: (T) -> Unit) {

    /**
     * Runs the lambda with the given input.
     *
     * @param input The input used for running the lambda.
     */
    fun onClick(input: T) = block(input)
}
