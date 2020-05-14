package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SuccessErrorViewModel
import kotlinx.coroutines.launch

class ItemListViewModel(
    private val itemRepository: ItemRepository,
    adminRepository: AdminRepository
) : SuccessErrorViewModel<String>() {

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
        viewModelScope.launch(exceptionHandler) {
            _refreshing.value = true
            itemRepository.refreshItems()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    fun triggerCreateItemAction() {
        viewModelScope.launch {
            _createItemAction.value = true
        }
    }

    fun onCreateItemActionHandled() {
        viewModelScope.launch {
            _createItemAction.value = false
        }
    }
}
