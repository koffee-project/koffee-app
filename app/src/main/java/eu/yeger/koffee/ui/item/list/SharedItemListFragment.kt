package eu.yeger.koffee.ui.item.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.home.SharedHomeFragmentDirections
import eu.yeger.koffee.ui.item.details.SharedItemDetailsFragment
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [ItemListFragment] for the multi user mode.
 * Supports searching.
 *
 * @property itemListViewModel The [MainItemListViewModel] used for accessing the item list.
 *
 * @author Jan Müller
 */
class SharedItemListFragment(private val userId: String) : ItemListFragment() {

    override val itemListViewModel: MainItemListViewModel by viewModelFactories {
        MainItemListViewModel(itemRepository = ItemRepository(requireContext()))
    }

    /**
     * No additional initialization.
     */
    override fun initializeViewModel() = Unit

    /**
     * Navigates to the [SharedItemDetailsFragment] for the given item id.
     *
     * @param itemId The id of the item.
     */
    override fun onItemSelected(itemId: String) {
        val direction = SharedHomeFragmentDirections.toSharedItemDetails(itemId, userId)
        findNavController().navigate(direction)
    }
}
