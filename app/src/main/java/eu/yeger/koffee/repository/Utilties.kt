package eu.yeger.koffee.repository

import eu.yeger.koffee.database.KoffeeDatabase

fun KoffeeDatabase.purgeUserById(userId: String) {
    userDao.deleteById(userId)
    transactionDao.deleteByUserId(userId)
}

fun KoffeeDatabase.purgeItemById(itemId: String) {
    itemDao.deleteById(itemId)
}
