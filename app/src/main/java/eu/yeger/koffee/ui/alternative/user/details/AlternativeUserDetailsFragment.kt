package eu.yeger.koffee.ui.alternative.user.details

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
import eu.yeger.koffee.ui.user.details.UserDetailsFragment
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class AlternativeUserDetailsFragment : UserDetailsFragment() {

    override val userId by lazy {
        AlternativeUserDetailsFragmentArgs.fromBundle(requireArguments()).userId
    }

    override val userDetailsViewModel: AlternativeUserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        AlternativeUserDetailsViewModel(
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
                val direction = AlternativeUserDetailsFragmentDirections.toAltUserList()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun initializeViewModel() {
        userDetailsViewModel.apply {
            observeAction(showItemsAction) {
                val direction = AlternativeUserDetailsFragmentDirections.toAltItemList()
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
                        AlternativeUserDetailsFragmentDirections.toAltItemDetails(itemId, userId)
                    findNavController().navigate(direction)
                }
            })
    }
}
