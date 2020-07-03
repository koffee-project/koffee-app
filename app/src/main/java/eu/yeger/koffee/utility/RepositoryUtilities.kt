package eu.yeger.koffee.utility

import retrofit2.HttpException

/**
 * Runs a lambda and catches 404 HTTP exceptions, running a second lambda.
 *
 * @param onNotFound Lambda executed when a 404 exception is caught.
 * @param rethrow Dictates whether caught exceptions other than 404 are rethrown. Defaults to true.
 * @param block Lambda that can throw 404 exceptions.
 *
 * @author Jan Müller
 */
inline fun onNotFound(onNotFound: () -> Unit, rethrow: Boolean = true, block: () -> Unit) {
    try {
        block()
    } catch (e: HttpException) {
        if (e.code() == 404) {
            onNotFound()
        }
        if (rethrow) {
            throw e
        }
    }
}
