package eu.yeger.koffee.network

import eu.yeger.koffee.domain.Item

/**
 * API class for purchases.
 *
 * @property itemId The id of the purchased [Item].
 * @property amount The amount purchased.
 *
 * @author Jan Müller
 */
data class ApiPurchaseRequest(
    val itemId: String,
    val amount: Int
)
