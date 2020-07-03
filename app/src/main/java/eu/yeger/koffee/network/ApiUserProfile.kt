package eu.yeger.koffee.network

import eu.yeger.koffee.domain.User

/**
 * API class for metadata and balance of [User]s.
 *
 * @property id The id of the [User].
 * @property name The name of the [User].
 * @property balance The balance of the [User].
 *
 * @author Jan Müller
 */
data class ApiUserProfile(
    val id: String,
    val name: String,
    val balance: Double
)

/**
 * Extension method for transforming [ApiUserProfile]s into [User]s.
 *
 * @receiver The source [ApiUserProfile].
 * @return The [User].
 *
 * @author Jan Müller
 */
fun ApiUserProfile.asDomainModel() = User(
    id = id,
    name = name,
    balance = balance
)
