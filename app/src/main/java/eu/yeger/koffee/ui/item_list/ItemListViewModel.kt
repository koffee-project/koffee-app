package eu.yeger.koffee.ui.item_list

import androidx.lifecycle.*
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.RepositoryState
import kotlinx.coroutines.launch

class ItemListViewModel(
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

    class Factory(
        private val itemRepository: ItemRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ItemListViewModel(itemRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}