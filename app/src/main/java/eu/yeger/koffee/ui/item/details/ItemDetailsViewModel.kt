package eu.yeger.koffee.ui.item.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.paging.toLiveData
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction

private const val PAGE_SIZE = 50

/**
 * Abstract [CoroutineViewModel] for accessing and refreshing item data.
 *
 * @property itemId The id of the item.
 * @property userId Optional user id for purchases.
 * @property itemRepository [ItemRepository] for accessing and refreshing item data.
 * @property transactionRepository [TransactionRepository] for accessing and refreshing transactions.
 * @property user A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the user.
 * @property hasUser Indicates that the user is non-null.
 * @property item A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the item.
 * @property hasItem Indicates that the item is non-null
 * @property transactions A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the transaction for this item and user.
 * @property hasTransactions Indicates that the transactions are not empty.
 * @property canModify Indicates that the user can be modified. Abstract.
 * @property refreshing Indicates that a refresh is in progress.
 * @property itemNotFoundAction [SimpleAction] that is activated when the item has been deleted.
 * @param userRepository [UserRepository] for accessing user data.
 *
 * @author Jan Müller
 */
abstract class ItemDetailsViewModel(
    private val itemId: String,
    private val userId: String?,
    private val itemRepository: ItemRepository,
    private val transactionRepository: TransactionRepository,
    userRepository: UserRepository
) : CoroutineViewModel() {

    val user = userRepository.getUserByIdFlow(userId).asLiveData()
    val hasUser = user.map { it != null }

    val item = itemRepository.getItemByIdFlow(itemId).asLiveData()
    val hasItem = item.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserIdAndItemId(userId, itemId)
        .toLiveData(PAGE_SIZE)
    val hasTransactions = transactions.map { it.isNotEmpty() }

    abstract val canModify: LiveData<Boolean>

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val itemNotFoundAction = SimpleAction()

    /**
     * Refreshes the data of the item with the given id.
     */
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

    /**
     * Requests a purchase of the item with the given id by the user with the given id.
     */
    fun buyItem() {
        userId?.let {
            onViewModelScope {
                transactionRepository.buyItem(userId, itemId, 1)
                itemRepository.fetchItemById(itemId)
                transactionRepository.fetchTransactionsByUserId(userId)
            }
        }
    }

    /**
     * Requests editing of the item.
     */
    abstract fun activateEditItemAction()

    /**
     * Requests deletion of the item.
     */
    abstract fun activateDeleteItemAction()
}
