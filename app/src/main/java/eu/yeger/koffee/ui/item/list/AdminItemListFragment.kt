package eu.yeger.koffee.ui.item.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class AdminItemListFragment : ItemListFragment() {

    override val itemListViewModel: AdminItemListViewModel by viewModelFactories {
        val context = requireContext()
        AdminItemListViewModel(
            adminRepository = AdminRepository(context),
            itemRepository = ItemRepository(context)
        )
    }

    override fun initializeViewModel() {
        itemListViewModel.apply {
            observeAction(createItemAction) {
                val direction = AdminItemListFragmentDirections.toItemCreation()
                findNavController().navigate(direction)
            }
        }
    }

    override fun onItemSelected(itemId: String) {
        val direction = AdminItemListFragmentDirections.toItemDetails(itemId)
        findNavController().navigate(direction)
    }
}
