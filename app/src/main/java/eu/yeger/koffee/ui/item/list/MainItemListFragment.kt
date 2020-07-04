package eu.yeger.koffee.ui.item.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.home.MainHomeFragmentDirections
import eu.yeger.koffee.ui.item.details.MainItemDetailsFragment
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [ItemListFragment] for the single user mode.
 * Supports searching.
 *
 * @property itemListViewModel The [MainItemListViewModel] used for accessing the item list.
 *
 * @author Jan Müller
 */
class MainItemListFragment : ItemListFragment() {

    override val itemListViewModel: MainItemListViewModel by viewModelFactories {
        MainItemListViewModel(itemRepository = ItemRepository(requireContext()))
    }

    /**
     * No additional initialization.
     */
    override fun initializeViewModel() = Unit

    /**
     * Navigates to the [MainItemDetailsFragment] for the given item id.
     *
     * @param itemId The id of the item.
     */
    override fun onItemSelected(itemId: String) {
        val direction = MainHomeFragmentDirections.toItemDetails(itemId)
        findNavController().navigate(direction)
    }
}
