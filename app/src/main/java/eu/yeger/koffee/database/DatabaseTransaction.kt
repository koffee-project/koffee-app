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

fun DatabaseTransaction.asDomainModel(): Transaction {
    return when (type) {
        Transaction.Type.funding -> Transaction.Funding(
            userId = userId,
            value = value,
            timestamp = timestamp
        )
        Transaction.Type.purchase -> Transaction.Purchase(
            userId = userId,
            value = value,
            timestamp = timestamp,
            itemId = itemId!!,
            amount = amount!!
        )
        Transaction.Type.refund -> Transaction.Refund(
            userId = userId,
            value = value,
            timestamp = timestamp,
            itemId = itemId!!,
            amount = amount!!
        )
    }
}

fun List<DatabaseTransaction>.asDomainModel() = map { it.asDomainModel() }
