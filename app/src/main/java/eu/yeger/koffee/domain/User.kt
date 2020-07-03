package eu.yeger.koffee.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Entity](https://developer.android.com/reference/androidx/room/Entity) class representing [User]s.
 *
 * @property id The id of the [User].
 * @property name The name of the [User].
 * @property balance The user's current balance.
 *
 * @author Jan Müller
 */
@Entity
data class User(
    @PrimaryKey
    val id: String,
    val name: String,
    val balance: Double
)
