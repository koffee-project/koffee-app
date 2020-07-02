package eu.yeger.koffee.ui.item.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.ItemRepository

class MainItemListViewModel(
    itemRepository: ItemRepository
) : ItemListViewModel(itemRepository) {

    override val isAuthenticated: LiveData<Boolean>
        get() = MutableLiveData(false)

    override fun activateCreateItemAction() = Unit
}
