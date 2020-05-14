package eu.yeger.koffee.ui.item.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemListBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.ItemListAdapter
import eu.yeger.koffee.ui.onError
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

class ItemListFragment : Fragment() {

    private val itemListViewModel: ItemListViewModel by viewModelFactories {
        val context = requireContext()
        ItemListViewModel(
            adminRepository = AdminRepository(context),
            itemRepository = ItemRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemListViewModel.apply {
            createItemAction.observe(viewLifecycleOwner, Observer { createItem ->
                if (createItem) {
                    val action =
                        ItemListFragmentDirections.toItemCreation()
                    findNavController().navigate(action)
                    onCreateItemActionHandled()
                }
            })

            onError(this@ItemListFragment) { error ->
                requireActivity().showSnackbar(getString(R.string.item_refresh_error_format, error))
            }
        }

        return FragmentItemListBinding.inflate(inflater).apply {
            viewModel = itemListViewModel
            itemRecyclerView.adapter = ItemListAdapter(OnClickListener { selectedItem ->
                val action =
                    ItemListFragmentDirections.toItemDetails(
                        selectedItem.id
                    )
                findNavController().navigate(action)
            })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
