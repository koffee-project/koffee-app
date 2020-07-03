package eu.yeger.koffee.utility

/**
 * Checks if a [Double] is a valid currency amount.
 *
 * @receiver The [Double] to be checked.
 * @return true if the [Double] is a valid currency amount.
 *
 * @author Jan Müller
 */
fun Double?.isValidCurrencyAmount() =
    this != null &&
            isFinite() &&
            this >= 0 &&
            (this * 100).toInt().toDouble() == this * 100
