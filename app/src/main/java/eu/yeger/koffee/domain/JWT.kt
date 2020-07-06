package eu.yeger.koffee.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Entity](https://developer.android.com/reference/androidx/room/Entity) class representing [JWT]s.
 *
 * @property userId The id of the [User] owning this token.
 * @property token The token.
 * @property expiration The expiration date of the token.
 *
 * @author Jan Müller
 */
@Entity
data class JWT(
    @PrimaryKey
    val userId: String,
    val token: String,
    val expiration: Long
)
