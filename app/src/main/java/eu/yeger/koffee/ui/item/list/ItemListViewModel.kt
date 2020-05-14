package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.CoroutineViewModel

class ItemListViewModel(
    private val itemRepository: ItemRepository,
    adminRepository: AdminRepository
) : CoroutineViewModel() {

    val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    val items = itemRepository.items

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    private val _createItemAction = MutableLiveData(false)
    val createItemAction: LiveData<Boolean> = _createItemAction

    init {
        refreshItems()
    }

    fun refreshItems() {
        launchOnViewModelScope {
            _refreshing.value = true
            itemRepository.refreshItems()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    fun triggerCreateItemAction() = _createItemAction.postValue(true)

    fun onCreateItemActionHandled() = _createItemAction.postValue(false)
}
