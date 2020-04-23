package eu.yeger.koffee.network

import eu.yeger.koffee.domain.UserListEntry

data class KoffeeApiUserListEntry(
    val id: String,
    val name: String
)

fun KoffeeApiUserListEntry.asDomainUserListEntry() = UserListEntry(
    id = id,
    name = name
)
