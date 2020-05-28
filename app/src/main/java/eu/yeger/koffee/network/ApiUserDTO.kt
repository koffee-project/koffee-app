package eu.yeger.koffee.network

data class ApiUserDTO(
    val id: String?,
    val name: String,
    val password: String?,
    val isAdmin: Boolean
)
