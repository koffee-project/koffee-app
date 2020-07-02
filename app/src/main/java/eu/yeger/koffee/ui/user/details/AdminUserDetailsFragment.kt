package eu.yeger.koffee.ui.user.details

import android.app.AlertDialog
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.utility.*

class AdminUserDetailsFragment : UserDetailsFragment() {

    override val userId by lazy {
        AdminUserDetailsFragmentArgs.fromBundle(requireArguments()).userId
    }

    override val userDetailsViewModel: AdminUserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        AdminUserDetailsViewModel(
            isActiveUser = false,
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
                val direction = AdminUserDetailsFragmentDirections.toUserEditing(userId)
                findNavController().navigate(direction)
            }

            observeAction(creditUserAction) { userId ->
                val direction = AdminUserDetailsFragmentDirections.toUserCrediting(userId)
                findNavController().navigate(direction)
            }

            observeAction(deleteUserAction) { userId ->
                showDeleteDialog(userId) {
                    userDetailsViewModel.deleteUser()
                }
            }

            observeAction(userDeletedAction) {
                requireActivity().showSnackbar(getString(R.string.user_deletion_success))
                val direction = AdminUserDetailsFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }

            onAuthorizationException {
                hideKeyboard()
                requireActivity().showSnackbar(R.string.login_expired)
                val direction = AdminUserDetailsFragmentDirections.toSettings()
                direction.loginExpired = true
                findNavController().navigate(direction)
            }
        }
    }

    override fun FragmentUserDetailsBinding.initializeBinding() {
        refundViewModel = null
        transactionRecyclerView.adapter = transactionListAdapter()
    }

    override fun onNotFound() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.user_not_found)
            .setPositiveButton(R.string.go_back) { _, _ ->
                val direction = AdminUserDetailsFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
