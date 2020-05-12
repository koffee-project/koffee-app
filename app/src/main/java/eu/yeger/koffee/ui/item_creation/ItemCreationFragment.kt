package eu.yeger.koffee.ui.item_creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemCreationBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.utility.showSnackbar

class ItemCreationFragment : Fragment() {

    private val itemCreationViewModel: ItemCreationViewModel by viewModels {
        val context = requireContext()
        ItemCreationViewModel.Factory(
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
            successAction.observe(viewLifecycleOwner, Observer { itemId ->
                itemId?.let {
                    requireActivity().showSnackbar(getString(R.string.item_creation_success))
                    val action = ItemCreationFragmentDirections.toItemDetails(itemId)
                    findNavController().navigate(action)
                    onSuccessActionHandled()
                }
            })

            errorAction.observe(viewLifecycleOwner, Observer { error ->
                error?.let {
                    requireActivity().showSnackbar(error)
                    onErrorActionHandled()
                }
            })
        }

        return FragmentItemCreationBinding.inflate(inflater).apply {
            viewModel = itemCreationViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
