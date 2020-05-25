package eu.yeger.koffee.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JWT(
    @PrimaryKey
    val userId: String,
    val token: String,
    val expiration: Long
)
