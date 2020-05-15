package eu.yeger.koffee.utility

import retrofit2.HttpException

inline fun onNotFound(onNotFound: () -> Unit, block: () -> Unit) {
    try {
        block()
    } catch (e: HttpException) {
        if (e.code() == 404) {
            onNotFound()
        }
        throw e
    }
}
