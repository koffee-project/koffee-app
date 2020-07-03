package eu.yeger.koffee.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.domain.User

/**
 * Entity class for [Transaction]s.
 *
 * @property primaryKey Auto-generated primary key of this entity.
 * @property userId The ID of the [User] this [Transaction] belongs to.
 * @property type The type of this [Transaction].
 * @property value The value of this [Transaction].
 * @property timestamp The timestamp of this [Transaction].
 * @property itemId The id of the item belonging to this [Transaction]. Null for [Transaction.Type.Funding]s.
 * @property itemName The name of the item belonging to this [Transaction]. Null for [Transaction.Type.Funding]s.
 * @property amount The amount of the item belonging to this [Transaction]. Null for [Transaction.Type.Funding]s.
 *
 * @author Jan Müller
 */
@Entity
data class DatabaseTransaction(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Long = 0,
    val userId: String,
    val type: Transaction.Type,
    val value: Double,
    val timestamp: Long,
    val itemId: String?,
    val itemName: String?,
    val amount: Int?
)

/**
 * Extension method for turning [DatabaseTransaction]s into [Transaction]s.
 *
 * @receiver The source [DatabaseTransaction].
 *
 * @author Jan Müller
 */
fun DatabaseTransaction.asDomainModel(): Transaction {
    return when (type) {
        Transaction.Type.Funding -> Transaction.Funding(
            userId = userId,
            value = value,
            timestamp = timestamp
        )
        Transaction.Type.Purchase -> Transaction.Purchase(
            userId = userId,
            value = value,
            timestamp = timestamp,
            itemId = itemId!!,
            itemName = itemName ?: itemId,
            amount = amount!!
        )
        Transaction.Type.Refund -> Transaction.Refund(
            userId = userId,
            value = value,
            timestamp = timestamp,
            itemId = itemId!!,
            itemName = itemName ?: itemId,
            amount = amount!!
        )
    }
}
