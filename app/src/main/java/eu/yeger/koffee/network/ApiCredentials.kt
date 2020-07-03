package eu.yeger.koffee.network

import eu.yeger.koffee.domain.User

/**
 * API class for login-credentials.
 *
 * @property id The [User]'s id.
 * @property password The [User]'s password.
 *
 * @author Jan Müller
 */
data class ApiCredentials(
    val id: String,
    val password: String
)
