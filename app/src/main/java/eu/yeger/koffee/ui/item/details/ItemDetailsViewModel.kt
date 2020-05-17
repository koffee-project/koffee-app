package eu.yeger.koffee.ui.item.details

import android.os.CountDownTimer
import androidx.lifecycle.*
import eu.yeger.koffee.BuildConfig
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.singleTickTimer
import eu.yeger.koffee.utility.sourcedLiveData

class ItemDetailsViewModel(
    private val itemId: String,
    private val userId: String?,
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository,
    private val transactionRepository: TransactionRepository,
    userRepository: UserRepository
) : CoroutineViewModel() {

    private val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    val user = userRepository.getUserByIdAsLiveData(userId)
    val hasUser = user.map { it != null }

    val item = itemRepository.getItemByIdAsLiveData(itemId)
    val hasItem = item.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserIdAndItemIdAsLiveData(userId, itemId)
    val hasTransactions = transactions.map { it.isNotEmpty() }

    private var refundTimer: CountDownTimer? = null

    private val isWithinRefundInterval = MutableLiveData(true)

    private val hasRefundable =
        transactionRepository.getLastRefundableTransactionByUserIdAsLiveData(userId).map { transaction ->
            refundTimer?.cancel()
            when {
                transaction === null -> false // no refundable
                transaction.itemId != itemId -> false // refundable is not this item
                else -> {
                    val elapsedTime = System.currentTimeMillis() - transaction.timestamp
                    val remainingTime = BuildConfig.REFUND_INTERVAL - elapsedTime
                    when {
                        remainingTime > 0 -> {
                            isWithinRefundInterval.postValue(true)
                            refundTimer = singleTickTimer(remainingTime) {
                                isWithinRefundInterval.postValue(false)
                            }.start()
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

    val canModify = sourcedLiveData(isAuthenticated, hasItem) {
        isAuthenticated.value == true && hasItem.value == true
    }

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val editItemAction = DataAction<String>()
    val deleteItemAction = DataAction<String>()
    val itemDeletedAction = SimpleAction()
    val itemNotFoundAction = SimpleAction()

    fun refreshItem() {
        onViewModelScope {
            _refreshing.value = true
            itemRepository.fetchItemById(itemId)
        }.invokeOnCompletion {
            _refreshing.postValue(false)
            onViewModelScope {
                if (!itemRepository.hasItemWithId(itemId)) {
                    itemNotFoundAction.activate()
                }
            }
        }
    }

    fun buyItem() {
        userId?.let {
            onViewModelScope {
                transactionRepository.buyItem(userId, itemId, 1)
                transactionRepository.fetchTransactionsByUserId(userId)
            }
        }
    }

    fun refundPurchase() {
        userId?.let {
            onViewModelScope {
                transactionRepository.run {
                    refundPurchase(userId)
                    fetchTransactionsByUserId(userId)
                }
            }
        }
    }

    fun deleteItem() {
        onViewModelScope {
            val jwt = adminRepository.getJWT()!!
            itemRepository.deleteItem(itemId, jwt)
            itemDeletedAction.activate()
        }
    }

    fun activateEditItemAction() = editItemAction.activateWith(item.value?.id)

    fun activateDeleteItemAction() = deleteItemAction.activateWith(item.value?.id)

    override fun onCleared() {
        super.onCleared()
        refundTimer?.cancel()
    }
}
