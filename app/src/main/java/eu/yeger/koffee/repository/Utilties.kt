package eu.yeger.koffee.repository

import eu.yeger.koffee.database.KoffeeDatabase

/**
 * Removes all data related to the user with the given id from the database.
 *
 * @param userId The id of the user to be purged.
 *
 * @author Jan Müller
 */
suspend fun KoffeeDatabase.purgeUserById(userId: String) {
    userDao.deleteById(userId)
    transactionDao.deleteByUserId(userId)
    profileImageDao.deleteByUserId(userId)
}

/**
 * Removes all data related to the item with the given id from the database.
 *
 * @param itemId The id of the item to be purged.
 *
 * @author Jan Müller
 */
suspend fun KoffeeDatabase.purgeItemById(itemId: String) {
    itemDao.deleteById(itemId)
}
