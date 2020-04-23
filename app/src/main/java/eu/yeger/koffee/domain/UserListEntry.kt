package eu.yeger.koffee.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserListEntry(
    @PrimaryKey
    val id: String,
    val name: String
)
