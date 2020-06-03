package eu.yeger.koffee.ui.item.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class MainItemListFragment : ItemListFragment() {

    override val itemListViewModel: MainItemListViewModel by viewModelFactories {
        val context = requireContext()
        MainItemListViewModel(
            adminRepository = AdminRepository(context),
            itemRepository = ItemRepository(context)
        )
    }

    override fun initializeViewModel() {
        itemListViewModel.apply {
            observeAction(createItemAction) {
                val direction = MainItemListFragmentDirections.toItemCreation()
                findNavController().navigate(direction)
            }
        }
    }

    override fun onItemSelected(itemId: String) {
        val direction = MainItemListFragmentDirections.toItemDetails(itemId)
        findNavController().navigate(direction)
    }
}
