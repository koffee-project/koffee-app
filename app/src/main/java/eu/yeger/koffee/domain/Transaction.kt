package eu.yeger.koffee.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Long = 0,
    val userId: String,
    val type: Type,
    val value: Double,
    val timestamp: Long,
    val itemId: String?,
    val amount: Int?
) {
    enum class Type {
        funding,
        purchase,
        refund
    }
}
