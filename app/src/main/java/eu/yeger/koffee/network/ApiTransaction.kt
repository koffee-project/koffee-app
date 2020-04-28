package eu.yeger.koffee.network

import eu.yeger.koffee.domain.Transaction

data class ApiTransaction(
    val type: Transaction.Type,
    val value: Double,
    val timestamp: Long,
    val itemId: String?,
    val amount: Int?
)

fun List<ApiTransaction>.asDomainModel(userId: String) = map { transaction ->
    Transaction(
        userId = userId,
        type = transaction.type,
        value = transaction.value,
        timestamp = transaction.timestamp,
        itemId = transaction.itemId,
        amount = transaction.amount
    )
}
