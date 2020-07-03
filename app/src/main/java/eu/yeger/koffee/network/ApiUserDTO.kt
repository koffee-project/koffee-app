package eu.yeger.koffee.network

import eu.yeger.koffee.domain.User

/**
 * DTO API class for creating and updating [User]s.
 *
 * @property id The id of the [User].
 * @property name The name of the [User].
 * @property password The password of the [User].
 * @property isAdmin Indicates if this [User] is an admin.
 *
 * @author Jan Müller
 */
data class ApiUserDTO(
    val id: String?,
    val name: String,
    val password: String?,
    val isAdmin: Boolean
)
