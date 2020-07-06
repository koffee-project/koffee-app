package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SearchViewModel

private const val PAGE_SIZE = 50

/**
 * Abstract [SearchViewModel] for accessing available items.
 *
 * @property itemRepository [ItemRepository] for accessing, filtering and refreshing items.
 * @property isAuthenticated Indicates that a user is authenticated. Abstract.
 * @property refreshing Indicates that a refresh is in progress.
 *
 * @author Jan Müller
 */
abstract class ItemListViewModel(
    private val itemRepository: ItemRepository
) : SearchViewModel<Item>(itemRepository.getAllItems().toLiveData(PAGE_SIZE)) {

    abstract val isAuthenticated: LiveData<Boolean>

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    /**
     * Refreshes the available items.
     */
    fun refreshItems() {
        onViewModelScope {
            _refreshing.value = true
            itemRepository.fetchItems()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    /**
     * Requests the creation of items.
     */
    abstract fun activateCreateItemAction()

    /**
     * Get the items for the given filter.
     *
     * @param filter The filter derived from the search query.
     * @return The filtered items.
     */
    override fun getSource(filter: Filter): LiveData<PagedList<Item>> {
        return itemRepository.getFilteredItems(filter).toLiveData(PAGE_SIZE)
    }
}
