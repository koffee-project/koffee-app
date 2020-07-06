package eu.yeger.koffee.domain

/**
 * Data class containing information about purchase statistics.
 *
 * @property itemId The id of the [Item].
 * @property itemName The name of the [Item].
 * @property amount The total purchased amount of the [Item].
 * @property lastPurchaseTimestamp The timestamp of the last purchase.
 *
 * @author Jan Müller
 */
data class PurchaseStatistic(
    val itemId: String,
    val itemName: String,
    val amount: Int,
    val lastPurchaseTimestamp: Long
)
