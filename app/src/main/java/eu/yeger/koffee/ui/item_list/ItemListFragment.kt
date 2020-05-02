package eu.yeger.koffee.ui.item_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemListBinding
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.ItemListAdapter
import eu.yeger.koffee.utility.showRefreshResultSnackbar

class ItemListFragment : Fragment() {

    private val itemListViewModel: ItemListViewModel by viewModels {
        val itemRepository = ItemRepository(requireContext())

        ItemListViewModel.Factory(
            itemRepository = itemRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemListViewModel.refreshResultAction.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                showRefreshResultSnackbar(
                    repositoryState = state,
                    successText = R.string.item_refresh_success,
                    errorTextFormat = R.string.item_refresh_error_format
                )
                itemListViewModel.onRefreshResultActionHandled()
            }
        })

        val binding = FragmentItemListBinding.inflate(inflater)
        binding.viewModel = itemListViewModel
        binding.itemRecyclerView.adapter =
            ItemListAdapter(OnClickListener { selectedItem ->
                val action = ItemListFragmentDirections.actionNavigationItemListToItemDetailsFragment(selectedItem.id)
                findNavController().navigate(action)
            })
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
