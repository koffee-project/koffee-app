package eu.yeger.koffee.utility

fun String?.nullIfBlank(): String? = when {
    isNullOrBlank() -> null
    else -> this
}
