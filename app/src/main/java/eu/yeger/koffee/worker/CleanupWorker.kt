package eu.yeger.koffee.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences

/**
 * Cleanup [Worker](https://developer.android.com/reference/androidx/work/Worker) that removed unused data from the local storage.
 *
 * @property appContext Context used for creating repositories.
 * @constructor
 * Used by [WorkManager](https://developer.android.com/jetpack/androidx/releases/work) to create instances.
 *
 * @param params Worker parameters. Not used by this worker.
 *
 * @author Jan Müller
 */
class CleanupWorker(
    private val appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val itemRepository = ItemRepository(appContext)
    private val profileImageRepository = ProfileImageRepository(appContext)
    private val transactionRepository = TransactionRepository(appContext)
    private val userRepository = UserRepository(appContext)

    /**
     * Refreshed local data and removes data not required by the active user.
     *
     * @return The result of this worker.
     */
    override suspend fun doWork(): Result {
        val activeUserId = appContext.getUserIdFromSharedPreferences()
        itemRepository.fetchItems()
        userRepository.fetchUsers()
        profileImageRepository.deleteAllImagesExceptWithUserId(activeUserId)
        transactionRepository.deleteAllTransactionsExceptWithUserId(activeUserId)

        return Result.success()
    }
}
