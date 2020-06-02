package eu.yeger.koffee.ui.item.details

import androidx.lifecycle.asLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.sourcedLiveData

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

    fun deleteItem() {
        onViewModelScope {
            val jwt = adminRepository.getJWT()!!
            itemRepository.deleteItem(itemId, jwt)
            itemDeletedAction.activate()
        }
    }

    override fun activateEditItemAction() = editItemAction.activateWith(item.value?.id)

    override fun activateDeleteItemAction() = deleteItemAction.activateWith(item.value?.id)
}
