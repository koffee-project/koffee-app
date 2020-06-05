package eu.yeger.koffee.ui.item.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.home.HomeFragmentDirections
import eu.yeger.koffee.utility.viewModelFactories

class MainItemListFragment : ItemListFragment() {

    override val itemListViewModel: MainItemListViewModel by viewModelFactories {
        MainItemListViewModel(itemRepository = ItemRepository(requireContext()))
    }

    override fun initializeViewModel() = Unit

    override fun onItemSelected(itemId: String) {
        val direction = HomeFragmentDirections.toItemDetails(itemId)
        findNavController().navigate(direction)
    }
}
