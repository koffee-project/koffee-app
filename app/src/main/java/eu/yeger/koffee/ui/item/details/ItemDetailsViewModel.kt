package eu.yeger.koffee.ui.item.details

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

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

    private val _editItemAction = MutableLiveData<String?>(null)
    val editItemAction: LiveData<String?> = _editItemAction

    private val _deleteItemAction = MutableLiveData<String?>(null)
    val deleteItemAction: LiveData<String?> = _deleteItemAction

    private val _itemDeletedAction = MutableLiveData(false)
    val itemDeletedAction: LiveData<Boolean> = _itemDeletedAction

    private val _itemNotFoundAction = MutableLiveData(false)
    val itemNotFoundAction: LiveData<Boolean> = _itemNotFoundAction

    init {
        onViewModelScope {
            itemRepository.refreshItemById(itemId)
        }.invokeOnCompletion {
            viewModelScope.launch {
                _itemNotFoundAction.value = itemRepository.hasItemWithId(itemId).not()
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
            _itemDeletedAction.value = true
        }
    }

    fun triggerEditItemAction() = _editItemAction.postValue(item.value?.id)

    fun onEditItemActionHandled() = _editItemAction.postValue(null)

    fun triggerDeleteItemAction() = _deleteItemAction.postValue(item.value?.id)

    fun onDeleteItemActionHandled() = _deleteItemAction.postValue(null)

    fun onItemDeletedActionHandled() = _itemDeletedAction.postValue(false)

    fun onItemNotFoundActionHandled() = _itemNotFoundAction.postValue(false)
}
