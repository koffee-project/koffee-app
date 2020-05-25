package eu.yeger.koffee.utility

import org.ocpsoft.prettytime.PrettyTime
import java.util.*

fun String?.nullIfBlank(): String? = when {
    isNullOrBlank() -> null
    else -> this
}

fun formatTimestamp(timestamp: Long): String {
    val prettyTime = PrettyTime(Locale.getDefault())
    return prettyTime.format(Date(timestamp - 1000)) // subtract one second to prevent edge case issues
}
