package eu.yeger.koffee.network

import eu.yeger.koffee.database.DatabaseTransaction
import eu.yeger.koffee.domain.Transaction

/**
 * API class for [Transaction]s.
 *
 * @property type The type of this [Transaction].
 * @property value The value of this [Transaction].
 * @property timestamp The timestamp of this [Transaction].
 * @property itemId The id of the item belonging to this [Transaction]. Null for [Transaction.Type.Funding]s.
 * @property itemName The name of the item belonging to this [Transaction]. Null for [Transaction.Type.Funding]s.
 * @property amount The amount of the item belonging to this [Transaction]. Null for [Transaction.Type.Funding]s.
 *
 * @author Jan Müller
 */
data class ApiTransaction(
    val type: Transaction.Type,
    val value: Double,
    val timestamp: Long,
    val itemId: String?,
    val itemName: String?,
    val amount: Int?
)

/**
 * Extension method for transforming [ApiTransaction]s into [DatabaseTransaction]s.
 *
 * @receiver The source [ApiTransaction].
 * @return The [DatabaseTransaction].
 *
 * @author Jan Müller
 */
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

/**
 * Extension method for transforming a [List] of [ApiTransaction]s into a [List] of [DatabaseTransaction]s.
 *
 * @receiver The source [List] of [ApiTransaction].
 * @return The [List] of [DatabaseTransaction].
 *
 * @author Jan Müller
 */
fun List<ApiTransaction>.asDatabaseModel(userId: String) =
    map { it.asDatabaseModel(userId) }
