package eu.yeger.koffee.domain

sealed class Transaction {

    abstract val userId: String

    abstract val value: Double

    abstract val timestamp: Long

    data class Funding(
        override val userId: String,
        override val value: Double,
        override val timestamp: Long
    ) : Transaction()

    data class Purchase(
        override val userId: String,
        override val value: Double,
        override val timestamp: Long,
        val itemId: String,
        val amount: Int
    ) : Transaction()

    data class Refund(
        override val userId: String,
        override val value: Double,
        override val timestamp: Long,
        val itemId: String,
        val amount: Int
    ) : Transaction()

    enum class Type {
        funding,
        purchase,
        refund
    }
}
