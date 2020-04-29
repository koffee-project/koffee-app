package eu.yeger.koffee.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey
    val id: String,
    val amount: Int?,
    val price: Double
)
