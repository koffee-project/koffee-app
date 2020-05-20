package eu.yeger.koffee.utility

fun Double?.isValidCurrencyAmount() =
    this != null &&
            isFinite() &&
            this >= 0 &&
            (this * 100).toInt().toDouble() == this * 100
