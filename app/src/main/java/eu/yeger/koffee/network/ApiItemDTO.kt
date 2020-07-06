package eu.yeger.koffee.network

import eu.yeger.koffee.domain.Item

/**
 * DTO API class for creating and updating [Item]s.
 *
 * @property id The id of the [Item].
 * @property name The name of the [Item].
 * @property amount The optional amount of the [Item].
 * @property price The price of the [Item].
 *
 * @author Jan Müller
 */
class ApiItemDTO(
    val id: String?,
    val name: String,
    val price: Double,
    val amount: Int?
)
