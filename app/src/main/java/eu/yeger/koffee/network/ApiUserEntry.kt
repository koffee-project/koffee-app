package eu.yeger.koffee.network

import eu.yeger.koffee.domain.UserEntry

data class ApiUserEntry(
    val id: String,
    val name: String
)

fun ApiUserEntry.asDomainModel() = UserEntry(
    id = id,
    name = name
)
