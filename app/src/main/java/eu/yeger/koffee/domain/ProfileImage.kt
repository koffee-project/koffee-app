package eu.yeger.koffee.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ProfileImage(
    @PrimaryKey
    val userId: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val bytes: ByteArray,
    val timestamp: Long
)
