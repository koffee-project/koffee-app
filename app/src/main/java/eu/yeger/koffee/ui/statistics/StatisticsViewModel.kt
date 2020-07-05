package eu.yeger.koffee.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import eu.yeger.koffee.domain.PurchaseStatistic
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.ui.CoroutineViewModel

/**
 * ViewModel for accessing the purchase statistics of a user.
 *
 * @property userId The id of the user.
 * @property transactionRepository [TransactionRepository] for accessing and refreshing purchase statistics.
 * @property purchaseStatistics A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the purchase statistics of this user.
 * @property hasPurchaseStatistics Indicates that there are purchase statistics.
 * @property refreshing Indicates that a refresh is in progress.
 *
 * @author Jan Müller
 */
class StatisticsViewModel(
    private val userId: String,
    private val transactionRepository: TransactionRepository
) : CoroutineViewModel() {

    private val _purchaseStatistics = MutableLiveData<List<PurchaseStatistic>>(emptyList())
    val purchaseStatistics: LiveData<List<PurchaseStatistic>> = _purchaseStatistics
    val hasPurchaseStatistics = purchaseStatistics.map { it.isNotEmpty() }

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    /**
     * Refreshes the purchase statistics by fetching the transaction of this user.
     */
    fun refresh() {
        onViewModelScope {
            _refreshing.value = true
            transactionRepository.fetchTransactionsByUserId(userId)
            _purchaseStatistics.value = transactionRepository.getPurchaseStatsByUserId(userId)
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }
}
