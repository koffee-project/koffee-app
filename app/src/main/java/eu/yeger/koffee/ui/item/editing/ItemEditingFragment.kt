package eu.yeger.koffee.ui.item.editing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemEditingBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

class ItemEditingFragment : Fragment() {

    private val itemEditingViewModel: ItemEditingViewModel by viewModelFactories {
        val context = requireContext()
        ItemEditingViewModel(
            itemId = ItemEditingFragmentArgs.fromBundle(requireArguments()).itemId,
            adminRepository = AdminRepository(context),
            itemRepository = ItemRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemEditingViewModel.apply {
            observeAction(itemUpdatedAction) { itemId ->
                requireActivity().showSnackbar(getString(R.string.item_editing_success))
                val direction = ItemEditingFragmentDirections.toItemDetails(itemId)
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentItemEditingBinding.inflate(inflater).apply {
            viewModel = itemEditingViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
