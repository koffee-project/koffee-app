package eu.yeger.koffee.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import eu.yeger.koffee.BuildConfig
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.SimpleTimer
import eu.yeger.koffee.utility.sourcedLiveData

/**
 * ViewModel that enables refunding of purchases.
 *
 * @property itemId The id of an item. Optional. Should be null if this ViewModel is for users.
 * @property userId The id of the user.
 * @property itemRepository [ItemRepository] for refreshing item data after refunds.
 * @property transactionRepository [TransactionRepository] for refunding and evaluating refundable purchases.
 * @property userRepository [UserRepository] for refreshing user data after refunds.
 * @property refundable A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains a refundable [Transaction.Purchase].
 * @property canRefund Indicates that a refundable [Transaction.Purchase] exists.
 *
 * @author Jan Müller
 */
class RefundViewModel(
    private val itemId: String? = null,
    private val userId: String?,
    private val itemRepository: ItemRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    private var refundTimer = SimpleTimer {
        isWithinRefundInterval.postValue(false)
    }

    private val isWithinRefundInterval = MutableLiveData(true)

    val refundable =
        transactionRepository.getLastRefundableTransactionByUserIdFlow(userId)
            .asLiveData()
            .map { transaction ->
                refundTimer.stop()
                when {
                    transaction === null -> null // no refundable
                    itemId != null && itemId != transaction.itemId -> null // refundable is not this item
                    else -> {
                        val elapsedTime = System.currentTimeMillis() - transaction.timestamp
                        val remainingTime = BuildConfig.REFUND_INTERVAL - elapsedTime
                        when {
                            remainingTime > 0 -> {
                                isWithinRefundInterval.postValue(true)
                                refundTimer.start(remainingTime)
                                transaction
                            }
                            else -> null
                        }
                    }
                }
            }

    val canRefund = sourcedLiveData(isWithinRefundInterval, refundable) {
        isWithinRefundInterval.value == true && refundable.value !== null
    }

    /**
     * Requests a refund and refreshes item and user data.
     */
    fun refundPurchase() {
        val itemId = refundable.value?.itemId

        if (userId === null || itemId === null) return

        onViewModelScope {
            transactionRepository.run {
                refundPurchase(userId)
                fetchTransactionsByUserId(userId)
            }
            userRepository.fetchUserById(userId)
            itemRepository.fetchItemById(itemId)
        }
    }

    /**
     * Clears the ViewModel and stops any active timers.
     */
    override fun onCleared() {
        super.onCleared()
        refundTimer.stop()
    }
}
