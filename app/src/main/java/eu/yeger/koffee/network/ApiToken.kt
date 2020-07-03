package eu.yeger.koffee.network

import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.domain.User

/**
 * API class for [JWT]s.
 *
 * @property token The token.
 * @property expiration The expiration of this token.
 *
 * @author Jan Müller
 */
data class ApiToken(
    val token: String,
    val expiration: Long
)

/**
 * Extension method for transforming [ApiToken]s into [JWT]s.
 *
 * @receiver The source [ApiToken].
 * @param userId The id of the [User] this token belongs to.
 * @return The [JWT].
 *
 * @author Jan Müller
 */
fun ApiToken.asDomainModel(userId: String) = JWT(
    userId = userId,
    token = token,
    expiration = expiration
)
