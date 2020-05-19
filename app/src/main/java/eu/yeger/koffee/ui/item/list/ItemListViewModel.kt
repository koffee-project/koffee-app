package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SearchViewModel
import eu.yeger.koffee.ui.SimpleAction

private const val PAGE_SIZE = 50

class ItemListViewModel(
    private val itemRepository: ItemRepository,
    adminRepository: AdminRepository
) : SearchViewModel<Item>(itemRepository.getAllItemsPaged().toLiveData(PAGE_SIZE)) {

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

    override fun getSource(filter: Filter): LiveData<PagedList<Item>> {
        return itemRepository.getFilteredItemsPaged(filter).toLiveData(PAGE_SIZE)
    }
}
