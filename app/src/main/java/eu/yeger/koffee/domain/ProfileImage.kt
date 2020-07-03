package eu.yeger.koffee.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Entity](https://developer.android.com/reference/androidx/room/Entity) class representing [ProfileImage]s.
 *
 * @property userId The id of the image. Corresponds to a [User] id.
 * @property bytes The bytes of the image.
 * @property timestamp The timestamp of the image's upload date.
 *
 * @author Jan Müller
 */
@Entity
class ProfileImage(
    @PrimaryKey
    val userId: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val bytes: ByteArray,
    val timestamp: Long
)
