package eu.yeger.koffee.database

import androidx.room.TypeConverter
import eu.yeger.koffee.domain.Transaction

/**
 * Converter methods for enum types.
 *
 * @author Jan Müller
 */
class Converters {

    /**
     * Converts [Transaction.Type] to [String].
     *
     * @param type The source [Transaction.Type].
     * @return The matching [String].
     *
     * @author Jan Müller
     */
    @TypeConverter
    fun fromTransactionTypeToString(type: Transaction.Type): String =
        type.toString()

    /**
     * Converts [String] to [Transaction.Type].
     *
     * @param typeString The source [String].
     * @return The matching [Transaction.Type].
     *
     * @author Jan Müller
     */
    @TypeConverter
    fun fromStringToTransactionType(typeString: String): Transaction.Type =
        Transaction.Type.valueOf(typeString)
}
