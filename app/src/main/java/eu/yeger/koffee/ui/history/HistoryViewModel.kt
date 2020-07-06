package eu.yeger.koffee.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.toLiveData
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.ui.CoroutineViewModel

private const val PAGE_SIZE = 50

/**
 * [CoroutineViewModel] for accessing the transaction history of a user.
 *
 * @property userId The id of the user.
 * @property transactionRepository [TransactionRepository] for accessing and refreshing transactions.
 * @property transactions A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the transactions of this user.
 * @property hasTransactions Indicates that there are transactions.
 * @property refreshing Indicates that a refresh is in progress.
 *
 * @author Jan Müller
 */
class HistoryViewModel(
    private val userId: String,
    private val transactionRepository: TransactionRepository
) : CoroutineViewModel() {

    val transactions = transactionRepository.getTransactionsByUserId(userId).toLiveData(PAGE_SIZE)
    val hasTransactions = transactions.map { it.isNotEmpty() }

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    /**
     * Refreshes the history by fetching the transaction of this user.
     */
    fun refresh() {
        onViewModelScope {
            _refreshing.value = true
            transactionRepository.fetchTransactionsByUserId(userId)
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }
}
