package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.asLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SimpleAction

class MainItemListViewModel(
    adminRepository: AdminRepository,
    itemRepository: ItemRepository
) : ItemListViewModel(itemRepository) {

    override val isAuthenticated = adminRepository.isAuthenticatedFlow().asLiveData()

    val createItemAction = SimpleAction()

    override fun activateCreateItemAction() = createItemAction.activate()
}
