package eu.yeger.koffee.network

import eu.yeger.koffee.domain.User

data class ApiUserProfile(
    val id: String,
    val name: String,
    val balance: Double
)

fun ApiUserProfile.asDomainModel() = User(
    id = id,
    name = name,
    balance = balance
)
