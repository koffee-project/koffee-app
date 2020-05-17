package eu.yeger.koffee.ui.item.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemDetailsBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.utility.*

class ItemDetailsFragment : Fragment() {

    private val itemDetailsViewModel: ItemDetailsViewModel by viewModelFactories {
        val context = requireContext()
        ItemDetailsViewModel(
            itemId = ItemDetailsFragmentArgs.fromBundle(requireArguments()).itemId,
            userId = context.getUserIdFromSharedPreferences(),
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
                val direction = ItemDetailsFragmentDirections.toItemEditing(itemId)
                findNavController().navigate(direction)
            }

            observeAction(deleteItemAction) { itemId ->
                showDeleteDialog(itemId) {
                    itemDetailsViewModel.deleteItem()
                }
            }

            observeAction(itemDeletedAction) {
                requireActivity().showSnackbar(getString(R.string.item_deletion_success))
                val direction = ItemDetailsFragmentDirections.toItemList()
                findNavController().navigate(direction)
            }

            observeAction(itemNotFoundAction) {
                showItemNotFoundDialog()
            }

            onErrorShowSnackbar()
        }

        return FragmentItemDetailsBinding.inflate(inflater).apply {
            viewModel = itemDetailsViewModel
            transactionRecyclerView.adapter = transactionListAdapter(OnClickListener { /*ignore*/ })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        itemDetailsViewModel.refreshItem()
        super.onResume()
    }

    private fun showItemNotFoundDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.item_not_found))
            .setPositiveButton(R.string.go_back) { _, _ ->
                val direction = ItemDetailsFragmentDirections.toItemList()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
