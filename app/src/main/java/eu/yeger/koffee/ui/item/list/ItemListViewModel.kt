package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.*
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SearchViewModel
import eu.yeger.koffee.ui.SimpleAction

class ItemListViewModel(
    private val itemRepository: ItemRepository,
    adminRepository: AdminRepository
) : SearchViewModel<Item>(itemRepository.getItemsAsLiveData()) {

    val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val createItemAction = SimpleAction()

    fun refreshItems() {
        onViewModelScope {
            _refreshing.value = true
            itemRepository.fetchItems()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    fun activateCreateItemAction() = createItemAction.activate()

    override fun getSource(filter: Filter) = itemRepository.filteredUsers(filter)
}
