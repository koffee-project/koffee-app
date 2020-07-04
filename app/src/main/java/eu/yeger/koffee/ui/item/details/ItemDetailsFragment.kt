package eu.yeger.koffee.ui.item.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemDetailsBinding
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.RefundViewModel
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

/**
 * Abstract [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the item details screen.
 * Supports refunds.
 *
 * @property itemId The id of the item this [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) is for.
 * @property userId The optional id of the user this [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) is for.
 * @property itemDetailsViewModel The [ItemDetailsViewModel] used for accessing item information.
 *
 * @author Jan Müller
 */
abstract class ItemDetailsFragment : Fragment() {

    protected abstract val itemId: String

    protected abstract val userId: String?

    protected abstract val itemDetailsViewModel: ItemDetailsViewModel

    private val refundViewModel: RefundViewModel by viewModelFactories {
        val context = requireContext()
        RefundViewModel(
            itemId = itemId,
            userId = userId,
            itemRepository = ItemRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    /**
     * Called when the [ItemDetailsViewModel] is initialized.
     */
    protected abstract fun initializeViewModel()

    /**
     * Called when the not-found dialogue is confirmed.
     */
    protected abstract fun onNotFoundConfirmed()

    /**
     * Inflates and initializes the layout.
     *
     * @param inflater Used for layout inflation.
     * @param container Unused.
     * @param savedInstanceState Unused.
     * @return The item details view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemDetailsViewModel.apply {
            initializeViewModel()
            observeAction(itemNotFoundAction) { showItemNotFoundDialog() }
            onErrorShowSnackbar()
        }

        return FragmentItemDetailsBinding.inflate(inflater).apply {
            itemDetailsViewModel = this@ItemDetailsFragment.itemDetailsViewModel
            refundViewModel = this@ItemDetailsFragment.refundViewModel
            transactionRecyclerView.adapter = transactionListAdapter()
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    /**
     * Refreshes the [ItemDetailsViewModel].
     */
    override fun onResume() {
        itemDetailsViewModel.refreshItem()
        super.onResume()
    }

    private fun showItemNotFoundDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.item_not_found))
            .setPositiveButton(R.string.go_back) { _, _ ->
                onNotFoundConfirmed()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
