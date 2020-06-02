package eu.yeger.koffee.ui.item.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentItemDetailsBinding
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.RefundViewModel
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.utility.observeAction
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

    protected abstract fun initializeViewModel()

    protected abstract fun onNotFoundConfirmed()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemDetailsViewModel.apply {
            initializeViewModel()

            observeAction(itemNotFoundAction) {
                showItemNotFoundDialog()
            }

            onErrorShowSnackbar()
        }

        return FragmentItemDetailsBinding.inflate(inflater).apply {
            itemDetailsViewModel = this@ItemDetailsFragment.itemDetailsViewModel
            refundViewModel = this@ItemDetailsFragment.refundViewModel
            transactionRecyclerView.adapter = transactionListAdapter()
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
                onNotFoundConfirmed()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
