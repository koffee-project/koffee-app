package eu.yeger.koffee.database

import androidx.room.TypeConverter
import eu.yeger.koffee.domain.Transaction

class Converters {

    @TypeConverter
    fun fromTransactionTypeToString(type: Transaction.Type): String =
        type.toString()

    @TypeConverter
    fun fromStringToTransactionType(typeString: String): Transaction.Type =
        Transaction.Type.valueOf(typeString)
}
