package eu.yeger.koffee.network

import eu.yeger.koffee.domain.JWT

data class ApiToken(
    val token: String,
    val expiration: Long
)

fun ApiToken.asDomainModel(userId: String) = JWT(
    userId = userId,
    token = token,
    expiration = expiration
)
