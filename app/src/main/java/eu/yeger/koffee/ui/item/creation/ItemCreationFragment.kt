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
import eu.yeger.koffee.utility.hideKeyboard
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the item creation screen.
 *
 * @author Jan Müller
 */
class ItemCreationFragment : Fragment() {

    private val itemCreationViewModel: ItemCreationViewModel by viewModelFactories {
        val context = requireContext()
        ItemCreationViewModel(
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
     * @return The item creation view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemCreationViewModel.apply {
            observeAction(itemCreatedAction) { itemId ->
                hideKeyboard()
                requireActivity().showSnackbar(getString(R.string.item_creation_success))
                val direction = ItemCreationFragmentDirections.toItemDetails(itemId)
                findNavController().navigate(direction)
            }

            onAuthorizationException {
                hideKeyboard()
                requireActivity().showSnackbar(R.string.login_expired)
                val direction = ItemCreationFragmentDirections.toSettings()
                direction.loginExpired = true
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentItemCreationBinding.inflate(inflater).apply {
            viewModel = itemCreationViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
