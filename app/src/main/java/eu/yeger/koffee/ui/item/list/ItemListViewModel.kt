package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SearchViewModel

private const val PAGE_SIZE = 50

abstract class ItemListViewModel(
    private val itemRepository: ItemRepository
) : SearchViewModel<Item>(itemRepository.getAllItems().toLiveData(PAGE_SIZE)) {

    abstract val isAuthenticated: LiveData<Boolean>

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    fun refreshItems() {
        onViewModelScope {
            _refreshing.value = true
            itemRepository.fetchItems()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    abstract fun activateCreateItemAction()

    override fun getSource(filter: Filter): LiveData<PagedList<Item>> {
        return itemRepository.getFilteredItems(filter).toLiveData(PAGE_SIZE)
    }
}
