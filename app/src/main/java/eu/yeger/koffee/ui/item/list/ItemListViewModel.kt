package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.*
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.mediatedLiveData
import eu.yeger.koffee.utility.sourcedLiveData

class ItemListViewModel(
    private val itemRepository: ItemRepository,
    adminRepository: AdminRepository
) : CoroutineViewModel() {

    val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    private val items = itemRepository.getItemsAsLiveData()

    val searchQuery = MutableLiveData<String>()

    private val _isBusy: MutableLiveData<Boolean> = mediatedLiveData {
        addSource(items) { items: List<Item>? ->
            value = items?.size ?: 0 == 0 || value ?: false
        }
        value = true
    }
    val isBusy: LiveData<Boolean> = _isBusy

    val filteredItems = sourcedLiveData(items, searchQuery) {
        Filter(query = searchQuery.value ?: "")
    }.switchMap { filter ->
        _isBusy.value = true
        itemRepository.filteredUsers(filter)
    }
    val onFilteredUsersApplied = Runnable { _isBusy.postValue(false) }

    val hasResults: LiveData<Boolean> = sourcedLiveData(filteredItems) {
        filteredItems.value?.size ?: 0 > 0
    }
    val hasNoResults: LiveData<Boolean> = sourcedLiveData(filteredItems, isBusy) {
        filteredItems.value?.size == 0 && !(isBusy.value ?: false)
    }

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
}
