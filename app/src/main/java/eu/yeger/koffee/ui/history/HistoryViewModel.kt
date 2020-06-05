package eu.yeger.koffee.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.toLiveData
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.ui.CoroutineViewModel

private const val PAGE_SIZE = 50

class HistoryViewModel(
    private val userId: String,
    private val transactionRepository: TransactionRepository
) : CoroutineViewModel() {

    val transactions = transactionRepository.getTransactionsByUserId(userId).toLiveData(PAGE_SIZE)
    val hasTransactions = transactions.map { it.isNotEmpty() }

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    fun refresh() {
        onViewModelScope {
            transactionRepository.fetchTransactionsByUserId(userId)
        }
    }
}
