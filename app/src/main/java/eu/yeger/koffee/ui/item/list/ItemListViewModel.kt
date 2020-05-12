package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.RepositoryState
import kotlinx.coroutines.launch

class ItemListViewModel(
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {

    val items = itemRepository.items

    val refreshing = itemRepository.state.map { it is RepositoryState.Refreshing }

    val refreshResultAction = itemRepository.state.map { state ->
        when (state) {
            is RepositoryState.Done -> state
            is RepositoryState.Error -> state
            else -> null
        }
    }

    val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    private val _createItemAction = MutableLiveData(false)
    val createItemAction: LiveData<Boolean> = _createItemAction

    init {
        refreshItems()
    }

    fun refreshItems() {
        viewModelScope.launch {
            itemRepository.refreshItems()
        }
    }

    fun onRefreshResultActionHandled() {
        viewModelScope.launch {
            (refreshResultAction as MutableLiveData).value = null
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

    class Factory(
        private val adminRepository: AdminRepository,
        private val itemRepository: ItemRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ItemListViewModel(
                    adminRepository,
                    itemRepository
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
