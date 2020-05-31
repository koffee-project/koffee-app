package eu.yeger.koffee.utility

import retrofit2.HttpException

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
