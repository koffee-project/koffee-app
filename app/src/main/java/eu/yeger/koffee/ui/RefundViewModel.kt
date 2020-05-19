package eu.yeger.koffee.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import eu.yeger.koffee.BuildConfig
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.SimpleTimer
import eu.yeger.koffee.utility.sourcedLiveData

class RefundViewModel(
    private val itemId: String? = null,
    private val userId: String?,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    private var refundTimer = SimpleTimer {
        isWithinRefundInterval.postValue(false)
    }

    private val isWithinRefundInterval = MutableLiveData(true)

    private val hasRefundable =
        transactionRepository.getLastRefundableTransactionByUserIdFlow(userId)
            .asLiveData()
            .map { transaction ->
                refundTimer.stop()
                when {
                    transaction === null -> false // no refundable
                    itemId != null && itemId != transaction.itemId -> false // refundable is not this item
                    else -> {
                        val elapsedTime = System.currentTimeMillis() - transaction.timestamp
                        val remainingTime = BuildConfig.REFUND_INTERVAL - elapsedTime
                        when {
                            remainingTime > 0 -> {
                                isWithinRefundInterval.postValue(true)
                                refundTimer.start(remainingTime)
                                true
                            }
                            else -> false
                        }
                    }
                }
            }

    val canRefund = sourcedLiveData(isWithinRefundInterval, hasRefundable) {
        isWithinRefundInterval.value == true && hasRefundable.value == true
    }

    fun refundPurchase() {
        if (userId === null) return

        onViewModelScope {
            transactionRepository.run {
                refundPurchase(userId)
                fetchTransactionsByUserId(userId)
            }
            userRepository.fetchUserById(userId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        refundTimer.stop()
    }
}
