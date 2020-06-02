package eu.yeger.koffee.ui.user.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.utility.*

class MainUserDetailsFragment : UserDetailsFragment() {

    private val userId by lazy {
        MainUserDetailsFragmentArgs.fromBundle(requireArguments()).userId
    }

    override val userDetailsViewModel: MainUserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        MainUserDetailsViewModel(
            isActiveUser = false,
            userId = userId,
            adminRepository = AdminRepository(context),
            profileImageRepository = ProfileImageRepository(context),
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
            val direction = MainUserDetailsFragmentDirections.toHome()
            findNavController().navigate(direction)
            return null
        }

        userDetailsViewModel.apply {
            observeAction(editUserAction) { userId ->
                val direction = MainUserDetailsFragmentDirections.toUserEditing(userId)
                findNavController().navigate(direction)
            }

            observeAction(creditUserAction) { userId ->
                val direction = MainUserDetailsFragmentDirections.toUserCrediting(userId)
                findNavController().navigate(direction)
            }

            observeAction(deleteUserAction) { userId ->
                showDeleteDialog(userId) {
                    userDetailsViewModel.deleteUser()
                }
            }

            observeAction(userDeletedAction) {
                requireActivity().showSnackbar(getString(R.string.user_deletion_success))
                val direction = MainUserDetailsFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }

            observeAction(userNotFoundAction) {
                showUserNotFoundDialog()
            }

            onAuthorizationException {
                hideKeyboard()
                requireActivity().showSnackbar(R.string.login_expired)
                val direction = MainUserDetailsFragmentDirections.toAdmin()
                direction.loginExpired = true
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            userDetailsViewModel = this@MainUserDetailsFragment.userDetailsViewModel
            transactionRecyclerView.adapter = transactionListAdapter()
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onNotFoundConfirmed() {
        val direction = MainUserDetailsFragmentDirections.toUserList()
        findNavController().navigate(direction)
    }
}
