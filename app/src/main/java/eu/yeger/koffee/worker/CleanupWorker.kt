package eu.yeger.koffee.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences

class CleanupWorker(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val itemRepository = ItemRepository(appContext)
    private val transactionRepository = TransactionRepository(appContext)
    private val userRepository = UserRepository(appContext)

    override suspend fun doWork(): Result {
        val activeUserId = appContext.getUserIdFromSharedPreferences()
        itemRepository.fetchItems()
        userRepository.fetchUsers()
        transactionRepository.deleteAllTransactionsExceptWithUserId(activeUserId)
        return Result.success()
    }
}