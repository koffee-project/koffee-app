package eu.yeger.koffee.ui.item.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemDetailsBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.TransactionListAdapter
import eu.yeger.koffee.ui.onError
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

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
            deleteItemAction.observe(viewLifecycleOwner, Observer { itemName ->
                itemName?.let {
                    showFirstDeleteItemDialog(itemName)
                    onDeleteItemActionHandled()
                }
            })

            itemDeletedAction.observe(viewLifecycleOwner, Observer { itemDeleted ->
                if (itemDeleted) {
                    findNavController().navigateUp()
                    onItemDeletedActionHandled()
                }
            })

            itemNotFoundAction.observe(viewLifecycleOwner, Observer { itemNotFound ->
                if (itemNotFound) {
                    showItemNotFoundDialog()
                    onItemNotFoundActionHandled()
                }
            })

            onError(this@ItemDetailsFragment) { error ->
                requireActivity().showSnackbar(error)
            }
        }

        return FragmentItemDetailsBinding.inflate(inflater).apply {
            viewModel = itemDetailsViewModel
            transactionRecyclerView.adapter = TransactionListAdapter(OnClickListener { /*ignore*/ })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun showFirstDeleteItemDialog(itemName: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.delete_format, itemName))
            .setPositiveButton(R.string.delete) { _, _ ->
                showSecondDeleteItemDialog(itemName)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> /*ignore*/ }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showSecondDeleteItemDialog(itemName: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.delete_confirmation_format, itemName))
            .setPositiveButton(R.string.delete) { _, _ ->
                itemDetailsViewModel.deleteItem()
            }
            .setNegativeButton(R.string.cancel) { _, _ -> /*ignore*/ }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showItemNotFoundDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.item_not_found))
            .setPositiveButton(R.string.go_back) { _, _ ->
                findNavController().navigateUp()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
