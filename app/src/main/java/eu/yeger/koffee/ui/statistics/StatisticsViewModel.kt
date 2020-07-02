package eu.yeger.koffee.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import eu.yeger.koffee.domain.PurchaseStatistic
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.ui.CoroutineViewModel

class StatisticsViewModel(
    private val userId: String,
    private val transactionRepository: TransactionRepository
) : CoroutineViewModel() {

    private val _purchaseStatistics = MutableLiveData<List<PurchaseStatistic>>(emptyList())
    val purchaseStatistics: LiveData<List<PurchaseStatistic>> = _purchaseStatistics
    val hasPurchaseStatistics = purchaseStatistics.map { it.isNotEmpty() }

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    fun refresh() {
        onViewModelScope {
            _refreshing.value = true
            transactionRepository.fetchTransactionsByUserId(userId)
            val stats = transactionRepository.getPurchaseStatsByUserId(userId)
            _purchaseStatistics.value = stats
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }
}
