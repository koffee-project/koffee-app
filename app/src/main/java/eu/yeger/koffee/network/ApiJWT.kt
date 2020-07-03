package eu.yeger.koffee.network

import eu.yeger.koffee.domain.JWT

/**
 * Transforms a [JWT] into the correct header format.
 *
 * @receiver The source [JWT].
 * @return The formatted header [String].
 *
 * @author Jan Müller
 */
fun JWT.formatToken() = "Bearer $token"
