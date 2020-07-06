package eu.yeger.koffee.domain

import com.squareup.moshi.Json

/**
 * Sealed class for all types of [Transaction]s.
 *
 * @property userId The id of the [User] this [Transaction] belongs to.
 * @property value The value of the [Transaction].
 * @property timestamp The timestamp of the [Transaction].
 *
 * @author Jan Müller
 */
sealed class Transaction {

    abstract val userId: String

    abstract val value: Double

    abstract val timestamp: Long

    /**
     * Funding [Transaction].
     *
     * @property userId The id of the [User] this [Transaction] belongs to.
     * @property value The value of the [Transaction].
     * @property timestamp The timestamp of the [Transaction].
     *
     * @author Jan Müller
     */
    data class Funding(
        override val userId: String,
        override val value: Double,
        override val timestamp: Long
    ) : Transaction()

    /**
     * Purchase [Transaction].
     *
     * @property userId The id of the [User] this [Transaction] belongs to.
     * @property value The value of the [Transaction].
     * @property timestamp The timestamp of the [Transaction].
     * @property itemId The id of the purchased [Item].
     * @property itemName The name of the purchased [Item].
     * @property amount The purchased amount.
     *
     * @author Jan Müller
     */
    data class Purchase(
        override val userId: String,
        override val value: Double,
        override val timestamp: Long,
        val itemId: String,
        val itemName: String,
        val amount: Int
    ) : Transaction()

    /**
     * Refund [Transaction].
     *
     * @property userId The id of the [User] this [Transaction] belongs to.
     * @property value The value of the [Transaction].
     * @property timestamp The timestamp of the [Transaction].
     * @property itemId The id of the refunded [Item].
     * @property itemName The name of the refunded [Item].
     * @property amount The refunded amount.
     *
     * @author Jan Müller
     */
    data class Refund(
        override val userId: String,
        override val value: Double,
        override val timestamp: Long,
        val itemId: String,
        val itemName: String,
        val amount: Int
    ) : Transaction()

    /**
     * Enum class for converting from and to [Transaction]s.
     */
    enum class Type {
        @Json(name = "funding")
        Funding,
        @Json(name = "purchase")
        Purchase,
        @Json(name = "refund")
        Refund
    }
}
