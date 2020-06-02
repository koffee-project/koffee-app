package eu.yeger.koffee.ui.item.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemDetailsBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.adapter.transactionListAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

            observeAction(itemNotFoundAction) {
                showItemNotFoundDialog()
            }

            onAuthorizationException {
                hideKeyboard()
                requireActivity().showSnackbar(R.string.login_expired)
                val direction = MainItemDetailsFragmentDirections.toAdmin()
                direction.loginExpired = true
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentItemDetailsBinding.inflate(inflater).apply {
            itemDetailsViewModel = this@MainItemDetailsFragment.itemDetailsViewModel
            refundViewModel = this@MainItemDetailsFragment.refundViewModel
            transactionRecyclerView.adapter = transactionListAdapter()
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onNotFoundConfirmed() {
        val direction = MainItemDetailsFragmentDirections.toItemList()
        findNavController().navigate(direction)
    }
}
