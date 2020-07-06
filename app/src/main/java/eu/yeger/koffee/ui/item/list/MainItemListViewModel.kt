package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.ItemRepository

/**
 * [ItemListViewModel] for the single and multi user modes.
 *
 * @property isAuthenticated Always contains false.
 * @param itemRepository [ItemRepository] for accessing, filtering and refreshing items.
 *
 * @author Jan Müller
 */
class MainItemListViewModel(
    itemRepository: ItemRepository
) : ItemListViewModel(itemRepository) {

    override val isAuthenticated: LiveData<Boolean>
        get() = MutableLiveData(false)

    /**
     * Ignored.
     */
    override fun activateCreateItemAction() = Unit
}
