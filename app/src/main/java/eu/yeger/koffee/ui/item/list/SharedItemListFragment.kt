package eu.yeger.koffee.ui.item.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.utility.viewModelFactories

class SharedItemListFragment : ItemListFragment() {

    private val userId by lazy {
        SharedItemListFragmentArgs.fromBundle(requireArguments()).userId
    }

    override val itemListViewModel: SharedItemListViewModel by viewModelFactories {
        SharedItemListViewModel(ItemRepository(requireContext()))
    }

    override fun initializeViewModel() = Unit

    override fun onItemSelected(itemId: String) {
        val direction = SharedItemListFragmentDirections.toSharedItemDetails(itemId, userId)
        findNavController().navigate(direction)
    }
}
