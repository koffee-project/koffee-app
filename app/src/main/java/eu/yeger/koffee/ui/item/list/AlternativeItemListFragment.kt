package eu.yeger.koffee.ui.item.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.utility.viewModelFactories

class AlternativeItemListFragment : ItemListFragment() {

    private val userId by lazy {
        AlternativeItemListFragmentArgs.fromBundle(requireArguments()).userId
    }

    override val itemListViewModel: AlternativeItemListViewModel by viewModelFactories {
        AlternativeItemListViewModel(ItemRepository(requireContext()))
    }

    override fun initializeViewModel() = Unit

    override fun onItemSelected(itemId: String) {
        val direction = AlternativeItemListFragmentDirections.toAltItemDetails(itemId, userId)
        findNavController().navigate(direction)
    }
}
