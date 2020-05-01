package eu.yeger.koffee.network

import eu.yeger.koffee.database.DatabaseTransaction
import eu.yeger.koffee.domain.Transaction

data class ApiTransaction(
    val type: Transaction.Type,
    val value: Double,
    val timestamp: Long,
    val itemId: String?,
    val amount: Int?
)

fun List<ApiTransaction>.asDatabaseModel(userId: String) = map { apiTransaction ->
    DatabaseTransaction(
        userId = userId,
        type = apiTransaction.type,
        value = apiTransaction.value,
        timestamp = apiTransaction.timestamp,
        itemId = apiTransaction.itemId,
        amount = apiTransaction.amount
    )
}
