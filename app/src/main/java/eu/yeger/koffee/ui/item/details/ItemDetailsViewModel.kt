package eu.yeger.koffee.ui.item.details

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.SuccessErrorViewModel
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

class ItemDetailsViewModel(
    private val itemId: String,
    private val userId: String?,
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository,
    private val transactionRepository: TransactionRepository,
    userRepository: UserRepository
) : SuccessErrorViewModel<String>() {

    private val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    val user = userRepository.getUserByIdAsLiveData(userId)

    val hasUser = user.map { it != null }

    val item = itemRepository.getItemById(itemId)

    val hasItem = item.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserIdAndItemId(userId, itemId)

    val hasTransactions = transactions.map { it.isNotEmpty() }

    val canRefund = transactionRepository.getLastRefundableTransactionByUserId(userId)
        .map { it?.itemId == itemId }

    val canModify = sourcedLiveData(isAuthenticated, hasItem) {
        isAuthenticated.value ?: false && hasItem.value ?: false
    }

    private val _editItemAction = MutableLiveData(false)
    val editItemAction: LiveData<Boolean> = _editItemAction

    private val _deleteItemAction = MutableLiveData<String>(null)
    val deleteItemAction: LiveData<String> = _deleteItemAction

    private val _itemDeletedAction = MutableLiveData(false)
    val itemDeletedAction: LiveData<Boolean> = _itemDeletedAction

    private val _itemNotFoundAction = MutableLiveData(false)
    val itemNotFoundAction: LiveData<Boolean> = _itemNotFoundAction

    init {
        viewModelScope.launch {
            itemRepository.refreshItemById(itemId)
            _itemNotFoundAction.value = itemRepository.hasItemWithId(itemId).not()
        }
    }

    fun buyItem() {
        userId?.let {
            viewModelScope.launch {
                transactionRepository.buyItem(userId, itemId, 1)
                transactionRepository.fetchTransactionsByUserId(userId)
            }
        }
    }

    fun refundPurchase() {
        userId?.let {
            viewModelScope.launch {
                transactionRepository.run {
                    refundPurchase(userId)
                    fetchTransactionsByUserId(userId)
                }
            }
        }
    }

    fun triggerEditItemAction() {
        viewModelScope.launch {
            _editItemAction.value = true
        }
    }

    fun onEditItemActionHandled() {
        viewModelScope.launch {
            _editItemAction.value = false
        }
    }

    fun triggerDeleteItemAction() {
        viewModelScope.launch {
            _deleteItemAction.value = item.value?.name
        }
    }

    fun onDeleteItemActionHandled() {
        viewModelScope.launch {
            _deleteItemAction.value = null
        }
    }

    fun deleteItem() {
        viewModelScope.launch(exceptionHandler) {
            val jwt = adminRepository.getJWT()!!
            itemRepository.deleteItem(itemId, jwt)
            _itemDeletedAction.value = true
        }
    }

    fun onItemDeletedActionHandled() {
        viewModelScope.launch {
            _itemDeletedAction.value = false
        }
    }

    fun onItemNotFoundActionHandled() {
        viewModelScope.launch {
            _itemNotFoundAction.value = false
        }
    }
}
