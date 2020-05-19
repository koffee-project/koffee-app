package eu.yeger.koffee.network

import eu.yeger.koffee.database.DatabaseTransaction
import eu.yeger.koffee.domain.Transaction

data class ApiTransaction(
    val type: Transaction.Type,
    val value: Double,
    val timestamp: Long,
    val itemId: String?,
    val itemName: String?,
    val amount: Int?
)

fun ApiTransaction.asDatabaseModel(userId: String) =
    DatabaseTransaction(
        userId = userId,
        type = type,
        value = value,
        timestamp = timestamp,
        itemId = itemId,
        itemName = itemName,
        amount = amount
    )

fun List<ApiTransaction>.asDatabaseModel(userId: String) =
    map { it.asDatabaseModel(userId) }
