package eu.yeger.koffee.ui.item.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.item.details.MainItemDetailsFragment
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [ItemListFragment] for item management.
 * Supports searching.
 *
 * @property itemListViewModel The [AdminItemListViewModel] used for accessing the item list.
 *
 * @author Jan Müller
 */
class AdminItemListFragment : ItemListFragment() {

    override val itemListViewModel: AdminItemListViewModel by viewModelFactories {
        val context = requireContext()
        AdminItemListViewModel(
            adminRepository = AdminRepository(context),
            itemRepository = ItemRepository(context)
        )
    }

    /**
     * Initializes the [AdminItemListViewModel].
     */
    override fun initializeViewModel() {
        itemListViewModel.apply {
            observeAction(createItemAction) {
                val direction = AdminItemListFragmentDirections.toItemCreation()
                findNavController().navigate(direction)
            }
        }
    }

    /**
     * Navigates to the [MainItemDetailsFragment] for the given item id.
     *
     * @param itemId The id of the item.
     */
    override fun onItemSelected(itemId: String) {
        val direction = AdminItemListFragmentDirections.toItemDetails(itemId)
        findNavController().navigate(direction)
    }
}
