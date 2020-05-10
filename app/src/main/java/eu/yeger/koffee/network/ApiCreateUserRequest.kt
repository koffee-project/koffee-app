package eu.yeger.koffee.network

data class ApiCreateUserRequest(
    val id: String,
    val name: String,
    val password: String?,
    val isAdmin: Boolean
)
