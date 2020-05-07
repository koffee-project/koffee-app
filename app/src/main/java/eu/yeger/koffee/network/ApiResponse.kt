package eu.yeger.koffee.network

data class ApiResponse<Data>(
    val status: Status,
    val data: Data
) {
    data class Status(
        val value: Int,
        val description: String
    )
}
