package eu.yeger.koffee.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.yeger.koffee.domain.Transaction

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
