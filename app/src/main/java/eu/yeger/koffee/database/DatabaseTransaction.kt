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
    val amount: Int?
)

fun List<DatabaseTransaction>.asDomainModel() = map { databaseTransaction ->
    when (databaseTransaction.type) {
        Transaction.Type.funding -> Transaction.Funding(
            userId = databaseTransaction.userId,
            value = databaseTransaction.value,
            timestamp = databaseTransaction.timestamp
        )
        Transaction.Type.purchase -> Transaction.Purchase(
            userId = databaseTransaction.userId,
            value = databaseTransaction.value,
            timestamp = databaseTransaction.timestamp,
            itemId = databaseTransaction.itemId!!,
            amount = databaseTransaction.amount!!
        )
        Transaction.Type.refund -> Transaction.Refund(
            userId = databaseTransaction.userId,
            value = databaseTransaction.value,
            timestamp = databaseTransaction.timestamp,
            itemId = databaseTransaction.itemId!!,
            amount = databaseTransaction.amount!!
        )
    }
}
