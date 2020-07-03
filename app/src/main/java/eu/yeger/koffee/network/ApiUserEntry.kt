package eu.yeger.koffee.network

import eu.yeger.koffee.domain.User

/**
 * API class for metadata of [User]s.
 *
 * @property id The id of the [User].
 * @property name The name of the [User].
 *
 * @author Jan Müller
 */
data class ApiUserEntry(
    val id: String,
    val name: String
)

/**
 * Extension method for transforming [ApiUserEntry]s into [User]s.
 *
 * @receiver The source [ApiUserEntry].
 * @return The [User].
 *
 * @author Jan Müller
 */
fun ApiUserEntry.asDomainModel() = User(
    id = id,
    name = name,
    balance = 0.0
)
