package eu.yeger.koffee.ui.user.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.utility.*

class UserDetailsFragment : Fragment() {

    private val userId by lazy {
        UserDetailsFragmentArgs.fromBundle(requireArguments()).userId
    }

    private val userDetailsViewModel: UserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        UserDetailsViewModel(
            isActiveUser = false,
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
    ): View? {
        if (userId == requireContext().getUserIdFromSharedPreferences()) {
            // current user is active user, so we navigate to home instead
            val direction = UserDetailsFragmentDirections.toHome()
            findNavController().navigate(direction)
            return null
        }

        userDetailsViewModel.apply {
            observeAction(editUserAction) { userId ->
                val direction = UserDetailsFragmentDirections.toUserEditing(userId)
                findNavController().navigate(direction)
            }

            observeAction(deleteUserAction) { userId ->
                showDeleteDialog(userId) {
                    userDetailsViewModel.deleteUser()
                }
            }

            observeAction(userDeletedAction) {
                requireActivity().showSnackbar(getString(R.string.user_deletion_success))
                val direction = UserDetailsFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }

            observeAction(userNotFoundAction) {
                showUserNotFoundDialog()
            }

            onErrorShowSnackbar()
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            userDetailsViewModel = this@UserDetailsFragment.userDetailsViewModel
            transactionRecyclerView.adapter = transactionListAdapter()
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        userDetailsViewModel.refreshUser()
        super.onResume()
    }

    private fun showUserNotFoundDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.user_not_found)
            .setPositiveButton(R.string.go_back) { _, _ ->
                val direction = UserDetailsFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
