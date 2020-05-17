package eu.yeger.koffee.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.ui.user.details.UserDetailsViewModel
import eu.yeger.koffee.utility.*

class HomeFragment : Fragment() {

    private val userId by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

    private val userDetailsViewModel: UserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        UserDetailsViewModel(
            isActiveUser = true,
            userId = userId,
            adminRepository = AdminRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userDetailsViewModel.apply {
            observeAction(editUserAction) { userId ->
                val direction = HomeFragmentDirections.toUserEditing(userId)
                findNavController().navigate(direction)
            }

            observeAction(userNotFoundAction) {
                val message = when (userId) {
                    null -> R.string.no_user_selected
                    else -> R.string.active_user_deleted
                }
                requireContext().deleteUserIdFromSharedPreferences()
                showUserSelectionRequiredDialog(message)
            }

            onErrorShowSnackbar()
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            viewModel = userDetailsViewModel
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
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        userDetailsViewModel.refreshUser()
        super.onResume()
    }

    private fun showUserSelectionRequiredDialog(message: Int) {
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
}
