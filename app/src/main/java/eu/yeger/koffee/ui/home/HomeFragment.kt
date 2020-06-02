package eu.yeger.koffee.ui.home

import android.app.AlertDialog
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.RefundViewModel
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.ui.user.details.MainUserDetailsViewModel
import eu.yeger.koffee.ui.user.details.UserDetailsFragment
import eu.yeger.koffee.utility.*

class HomeFragment : UserDetailsFragment() {

    private val userId by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

    private val refundViewModel: RefundViewModel by viewModelFactories {
        val context = requireContext()
        RefundViewModel(
            userId = userId,
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override val userDetailsViewModel: MainUserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        MainUserDetailsViewModel(
            isActiveUser = true,
            userId = userId,
            adminRepository = AdminRepository(context),
            profileImageRepository = ProfileImageRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun initializeViewModel() {
        userDetailsViewModel.apply {
            observeAction(editUserAction) { userId ->
                val direction = HomeFragmentDirections.toUserEditing(userId)
                findNavController().navigate(direction)
            }

            observeAction(creditUserAction) { userId ->
                val direction = HomeFragmentDirections.toUserCrediting(userId)
                findNavController().navigate(direction)
            }
        }
    }

    override fun FragmentUserDetailsBinding.initializeBinding() {
        refundViewModel = this@HomeFragment.refundViewModel
        transactionRecyclerView.adapter =
            transactionListAdapter(OnClickListener { selectedTransaction ->
                when (selectedTransaction) {
                    is Transaction.Purchase -> selectedTransaction.itemId
                    is Transaction.Refund -> selectedTransaction.itemId
                    else -> null
                }?.let { itemId ->
                    val direction = HomeFragmentDirections.toItemDetails(itemId)
                    findNavController().navigate(direction)
                }
            })
    }

    override fun onNotFound() {
        val message = when (userId) {
            null -> R.string.no_user_selected
            else -> R.string.active_user_deleted
        }
        requireContext().deleteUserIdFromSharedPreferences()
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.got_to_selection) { _, _ ->
                val direction = HomeFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun onResume() {
        userDetailsViewModel.refreshUser()
        super.onResume()
    }
}
