package eu.yeger.koffee.ui.item.details

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.*

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
                val direction = MainItemDetailsFragmentDirections.toItemList()
                findNavController().navigate(direction)
            }

            onAuthorizationException {
                hideKeyboard()
                requireActivity().showSnackbar(R.string.login_expired)
                val direction = MainItemDetailsFragmentDirections.toAdmin()
                direction.loginExpired = true
                findNavController().navigate(direction)
            }
        }
    }

    override fun onNotFoundConfirmed() {
        val direction = MainItemDetailsFragmentDirections.toItemList()
        findNavController().navigate(direction)
    }
}
