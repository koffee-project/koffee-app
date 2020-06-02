package eu.yeger.koffee.ui.item.details

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import eu.yeger.koffee.R
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.RefundViewModel
import eu.yeger.koffee.utility.viewModelFactories

abstract class ItemDetailsFragment : Fragment() {

    protected abstract val itemId: String

    protected abstract val userId: String?

    protected abstract val itemDetailsViewModel: ItemDetailsViewModel

    protected val refundViewModel: RefundViewModel by viewModelFactories {
        val context = requireContext()
        RefundViewModel(
            itemId = itemId,
            userId = userId,
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onResume() {
        itemDetailsViewModel.refreshItem()
        super.onResume()
    }

    protected fun showItemNotFoundDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.item_not_found))
            .setPositiveButton(R.string.go_back) { _, _ ->
                onNotFoundConfirmed()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    protected abstract fun onNotFoundConfirmed()
}
