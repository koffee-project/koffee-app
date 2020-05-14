package eu.yeger.koffee.utility

fun Double?.isValidPrice() =
    this != null &&
            isFinite() &&
            this >= 0 &&
            (this * 100).toInt().toDouble() == this * 100
