package eu.yeger.koffee.ui.item.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.home.SharedHomeFragmentDirections
import eu.yeger.koffee.utility.viewModelFactories

class SharedItemListFragment(private val userId: String) : ItemListFragment() {

    override val itemListViewModel by viewModelFactories {
        MainItemListViewModel(itemRepository = ItemRepository(requireContext()))
    }

    override fun initializeViewModel() = Unit

    override fun onItemSelected(itemId: String) {
        val direction = SharedHomeFragmentDirections.toSharedItemDetails(itemId, userId)
        findNavController().navigate(direction)
    }
}
