package eu.yeger.koffee.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Entity](https://developer.android.com/reference/androidx/room/Entity) class representing [Item]s.
 *
 * @property id The id of the [Item].
 * @property name The name of the [Item].
 * @property amount The optional amount of the [Item].
 * @property price The price of the [Item].
 *
 * @author Jan Müller
 */
@Entity
data class Item(
    @PrimaryKey
    val id: String,
    val name: String,
    val amount: Int?,
    val price: Double
)
