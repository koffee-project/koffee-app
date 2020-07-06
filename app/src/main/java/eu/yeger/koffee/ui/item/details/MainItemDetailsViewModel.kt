package eu.yeger.koffee.ui.item.details

import androidx.lifecycle.asLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.sourcedLiveData

/**
 * [ItemDetailsViewModel] for the single user mode and item management.
 *
 * @property itemId The id of the item.
 * @property adminRepository [AdminRepository] for accessing authentication tokens.
 * @property itemRepository [ItemRepository] for accessing and refreshing item data.
 * @property canModify Indicates that the user can be modified.
 * @property editItemAction [DataAction] that is activated when editing of the item is requested. Contains item's id.
 * @property deleteItemAction [DataAction] that is activated when deletion of the item is requested. Contains the item's id.
 * @property itemDeletedAction [SimpleAction] that is activated when the item has been deleted.
 * @param userId Optional user id for purchases.
 * @param transactionRepository [TransactionRepository] for accessing and refreshing transactions.
 * @param userRepository [UserRepository] for accessing user data.
 *
 * @author Jan Müller
 */
class MainItemDetailsViewModel(
    private val itemId: String,
    userId: String?,
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository,
    transactionRepository: TransactionRepository,
    userRepository: UserRepository
) : ItemDetailsViewModel(
    itemId = itemId,
    userId = userId,
    itemRepository = itemRepository,
    transactionRepository = transactionRepository,
    userRepository = userRepository
) {

    private val isAuthenticated = adminRepository.isAuthenticatedFlow().asLiveData()

    override val canModify = sourcedLiveData(isAuthenticated, hasItem) {
        isAuthenticated.value == true && hasItem.value == true
    }

    val editItemAction = DataAction<String>()
    val deleteItemAction = DataAction<String>()
    val itemDeletedAction = SimpleAction()

    /**
     * Requests the deletion of the item.
     */
    fun deleteItem() {
        onViewModelScope {
            val jwt = adminRepository.getJWT()!!
            itemRepository.deleteItem(itemId, jwt)
            itemDeletedAction.activate()
        }
    }

    /**
     * Requests editing of the item.
     */
    override fun activateEditItemAction() = editItemAction.activateWith(item.value?.id)

    /**
     * Requests deletion of the item.
     */
    override fun activateDeleteItemAction() = deleteItemAction.activateWith(item.value?.id)
}
