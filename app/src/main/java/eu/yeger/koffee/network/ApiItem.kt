package eu.yeger.koffee.network

import eu.yeger.koffee.domain.Item

/**
 * API class for [Item]s.
 *
 * @property id The id of the [Item].
 * @property name The name of the [Item].
 * @property amount The optional amount of the [Item].
 * @property price The price of the [Item].
 *
 * @author Jan Müller
 */
data class ApiItem(
    val id: String,
    val name: String,
    val amount: Int?,
    val price: Double
)

/**
 * Extension method for transforming [ApiItem]s into [Item]s.
 *
 * @receiver The source [ApiItem].
 *
 * @author Jan Müller
 */
fun ApiItem.asDomainModel() = Item(
    id = id,
    name = name,
    amount = amount,
    price = price
)

/**
 * Extension method for transforming a [List] of [ApiItem]s into a [List] of [Item]s.
 *
 * @receiver The source [List] of [ApiItem].
 *
 * @author Jan Müller
 */
fun List<ApiItem>.asDomainModel() = map { it.asDomainModel() }
