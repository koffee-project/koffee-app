package eu.yeger.koffee.utility

import java.util.*
import org.ocpsoft.prettytime.PrettyTime

/**
 * Turns blank [String]s into null values.
 *
 * @receiver The source [String].
 * @return null if the [String] is blank or the identity of the String otherwise.
 *
 * @author Jan Müller
 */
fun String?.nullIfBlank(): String? = when {
    isNullOrBlank() -> null
    else -> this
}

/**
 * Formats a UNIX-time timestamp into a human readable [String].
 *
 * @param timestamp The source UNIX-time timestamp.
 * @return The formatted timestamp [String].
 *
 * @author Jan Müller
 */
fun formatTimestamp(timestamp: Long): String {
    val prettyTime = PrettyTime(Locale.getDefault())
    return prettyTime.format(Date(timestamp - 1000)) // subtract one second to prevent edge case issues
}
