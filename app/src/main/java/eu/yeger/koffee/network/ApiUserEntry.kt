package eu.yeger.koffee.network

import eu.yeger.koffee.domain.User

data class ApiUserEntry(
    val id: String,
    val name: String
)

fun ApiUserEntry.asDomainModel() = User(
    id = id,
    name = name,
    balance = 0.0
)
