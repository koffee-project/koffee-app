package eu.yeger.koffee.ui.user.details

import android.app.AlertDialog
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class SharedUserDetailsFragment : UserDetailsFragment() {

    override val userId by lazy {
        SharedUserDetailsFragmentArgs.fromBundle(requireArguments()).userId
    }

    override val userDetailsViewModel: SharedUserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        SharedUserDetailsViewModel(
            userId = userId,
            profileImageRepository = ProfileImageRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onNotFound() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.user_not_found)
            .setPositiveButton(R.string.go_back) { _, _ ->
                val direction = SharedUserDetailsFragmentDirections.toSharedUserList()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun initializeViewModel() {
        userDetailsViewModel.apply {
            observeAction(showItemsAction) {
                val direction =
                    SharedUserDetailsFragmentDirections.toSharedItemList(userId)
                findNavController().navigate(direction)
            }
        }
    }

    override fun FragmentUserDetailsBinding.initializeBinding() {
        transactionRecyclerView.adapter =
            transactionListAdapter(OnClickListener { selectedTransaction ->
                when (selectedTransaction) {
                    is Transaction.Purchase -> selectedTransaction.itemId
                    is Transaction.Refund -> selectedTransaction.itemId
                    else -> null
                }?.let { itemId ->
                    val direction =
                        SharedUserDetailsFragmentDirections.toSharedItemDetails(
                            itemId,
                            userId
                        )
                    findNavController().navigate(direction)
                }
            })
    }
}
