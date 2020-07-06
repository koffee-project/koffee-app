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
import eu.yeger.koffee.utility.hideKeyboard
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the item editing screen.
 *
 * @author Jan Müller
 */
class ItemEditingFragment : Fragment() {

    private val itemEditingViewModel: ItemEditingViewModel by viewModelFactories {
        val context = requireContext()
        ItemEditingViewModel(
            itemId = ItemEditingFragmentArgs.fromBundle(requireArguments()).itemId,
            adminRepository = AdminRepository(context),
            itemRepository = ItemRepository(context)
        )
    }

    /**
     * Inflates and initializes the layout.
     *
     * @param inflater Used for layout inflation.
     * @param container Unused.
     * @param savedInstanceState Unused.
     * @return The login view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemEditingViewModel.apply {
            observeAction(itemUpdatedAction) { itemId ->
                hideKeyboard()
                requireActivity().showSnackbar(getString(R.string.item_editing_success))
                val direction = ItemEditingFragmentDirections.toItemDetails(itemId)
                findNavController().navigate(direction)
            }

            onAuthorizationException {
                hideKeyboard()
                requireActivity().showSnackbar(R.string.login_expired)
                val direction = ItemEditingFragmentDirections.toSettings()
                direction.loginExpired = true
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
