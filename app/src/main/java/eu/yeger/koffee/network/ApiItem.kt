package eu.yeger.koffee.network

import eu.yeger.koffee.domain.Item

data class ApiItem(
    val id: String,
    val name: String,
    val amount: Int?,
    val price: Double
)

fun ApiItem.asDomainModel() = Item(
    id = id,
    name = name,
    amount = amount,
    price = price
)

fun List<ApiItem>.asDomainModel() = map { it.asDomainModel() }
