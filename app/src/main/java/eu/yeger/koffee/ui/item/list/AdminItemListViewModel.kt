package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.asLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SimpleAction

/**
 * [ItemListViewModel] for item management.
 *
 * @property isAuthenticated Indicates that the user is authenticated.
 * @property createItemAction [SimpleAction] that is activated when the creation of an item is requested.
 * @param adminRepository [AdminRepository] for accessing authentication tokens.
 * @param itemRepository [ItemRepository] for accessing, filtering and refreshing items.
 *
 * @author Jan Müller
 */
class AdminItemListViewModel(
    adminRepository: AdminRepository,
    itemRepository: ItemRepository
) : ItemListViewModel(itemRepository) {

    override val isAuthenticated = adminRepository.isAuthenticatedFlow().asLiveData()

    val createItemAction = SimpleAction()

    /**
     * Requests the creation of an item.
     */
    override fun activateCreateItemAction() = createItemAction.activate()
}
