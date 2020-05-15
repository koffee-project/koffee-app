package eu.yeger.koffee.ui.item.details

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction
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

    val transactions = transactionRepository.getTransactionsByUserIdAndItemId(userId, itemId)
    val hasTransactions = transactions.map { it.isNotEmpty() }

    // TODO disable refund after expiry
    val canRefund = transactionRepository.getLastRefundableTransactionByUserId(userId)
        .map { it?.itemId == itemId }

    val canModify = sourcedLiveData(isAuthenticated, hasItem) {
        isAuthenticated.value ?: false && hasItem.value ?: false
    }

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val editItemAction = DataAction<String>()
    val deleteItemAction = DataAction<String>()
    val itemDeletedAction = SimpleAction()
    val itemNotFoundAction = SimpleAction()

    init {
        refreshItem()
    }

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

    fun triggerEditItemAction() = editItemAction.activateWith(item.value?.id)

    fun triggerDeleteItemAction() = deleteItemAction.activateWith(item.value?.id)
}
