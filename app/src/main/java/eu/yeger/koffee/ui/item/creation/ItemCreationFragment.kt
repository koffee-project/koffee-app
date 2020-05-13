package eu.yeger.koffee.ui.item.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemCreationBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.onError
import eu.yeger.koffee.ui.onSuccess
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

class ItemCreationFragment : Fragment() {

    private val itemCreationViewModel: ItemCreationViewModel by viewModelFactories {
        val context = requireContext()
        ItemCreationViewModel(
            adminRepository = AdminRepository(context),
            itemRepository = ItemRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemCreationViewModel.apply {
            onSuccess(this@ItemCreationFragment) { itemId ->
                requireActivity().showSnackbar(getString(R.string.item_creation_success))
                val action = ItemCreationFragmentDirections.toItemDetails(itemId)
                findNavController().navigate(action)
            }

            onError(this@ItemCreationFragment) { error ->
                requireActivity().showSnackbar(error)
            }
        }

        return FragmentItemCreationBinding.inflate(inflater).apply {
            viewModel = itemCreationViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
