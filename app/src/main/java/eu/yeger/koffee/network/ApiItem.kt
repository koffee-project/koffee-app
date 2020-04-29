package eu.yeger.koffee.network

import eu.yeger.koffee.domain.Item

data class ApiItem(
    val id: String,
    val amount: Int?,
    val price: Double
)

fun ApiItem.asDomainModel() = Item(
    id = id,
    amount = amount,
    price = price
)

fun List<ApiItem>.asDomainModel() = map { it.asDomainModel() }
