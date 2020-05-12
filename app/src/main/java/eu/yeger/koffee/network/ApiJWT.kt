package eu.yeger.koffee.network

import eu.yeger.koffee.domain.JWT

fun JWT.formatToken() = "Bearer $token"
