package eu.yeger.koffee.domain

data class PurchaseStatistic(
    val itemId: String,
    val itemName: String,
    val amount: Int,
    val lastPurchaseTimestamp: Long
)
