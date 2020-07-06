package eu.yeger.koffee.ui.item.details

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.home.MainHomeFragment
import eu.yeger.koffee.utility.*

/**
 * [ItemDetailsFragment] for the single user mode.
 *
 * @property itemId The id of the item from the arguments.
 * @property userId The id of the active user stored in the shared preferences.
 * @property itemDetailsViewModel The [MainItemDetailsViewModel] used for accessing item information.
 *
 * @author Jan Müller
 */
class MainItemDetailsFragment : ItemDetailsFragment() {

    override val itemId: String by lazy {
        MainItemDetailsFragmentArgs.fromBundle(requireArguments()).itemId
    }

    override val userId: String? by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

    override val itemDetailsViewModel: MainItemDetailsViewModel by viewModelFactories {
        val context = requireContext()
        MainItemDetailsViewModel(
            itemId = itemId,
            userId = userId,
            adminRepository = AdminRepository(context),
            itemRepository = ItemRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    /**
     * Initializes the [MainItemDetailsViewModel].
     */
    override fun initializeViewModel() {
        itemDetailsViewModel.apply {
            observeAction(editItemAction) { itemId ->
                val direction = MainItemDetailsFragmentDirections.toItemEditing(itemId)
                findNavController().navigate(direction)
            }

            observeAction(deleteItemAction) { itemId ->
                showDeleteDialog(itemId) {
                    itemDetailsViewModel.deleteItem()
                }
            }

            observeAction(itemDeletedAction) {
                requireActivity().showSnackbar(getString(R.string.item_deletion_success))
                val direction = MainItemDetailsFragmentDirections.toAdminItemList()
                findNavController().navigate(direction)
            }

            onAuthorizationException {
                hideKeyboard()
                requireActivity().showSnackbar(R.string.login_expired)
                val direction = MainItemDetailsFragmentDirections.toSettings()
                direction.loginExpired = true
                findNavController().navigate(direction)
            }
        }
    }

    /**
     * Navigates to the [MainHomeFragment].
     */
    override fun onNotFoundConfirmed() {
        val direction = MainItemDetailsFragmentDirections.toHome()
        findNavController().navigate(direction)
    }
}
