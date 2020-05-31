package eu.yeger.koffee.repository

import eu.yeger.koffee.database.KoffeeDatabase

suspend fun KoffeeDatabase.purgeUserById(userId: String) {
    userDao.deleteById(userId)
    transactionDao.deleteByUserId(userId)
    profileImageDao.deleteByUserId(userId)
}

suspend fun KoffeeDatabase.purgeItemById(itemId: String) {
    itemDao.deleteById(itemId)
}
