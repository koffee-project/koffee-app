package eu.yeger.koffee.ui.user.list

import android.app.AlertDialog
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.saveUserIdToSharedPreferences
import eu.yeger.koffee.utility.viewModelFactories

class AdminUserListFragment : UserListFragment() {

    override val userListViewModel: AdminUserListViewModel by viewModelFactories {
        val context = requireContext()
        AdminUserListViewModel(
            adminRepository = AdminRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun initializeViewModel() {
        userListViewModel.apply {
            observeAction(createUserAction) {
                val direction = AdminUserListFragmentDirections.toUserCreation()
                findNavController().navigate(direction)
            }

            observeAction(userSelectedAction) { pair ->
                val (isAuthenticated, user) = pair
                showUserSelectionDialog(user, isAuthenticated)
            }
        }
    }

    private fun showUserSelectionDialog(user: User, isAuthenticated: Boolean) {
        val message = getString(R.string.set_active_user_format, user.name, user.id)
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.set_as_active_user) { _, _ ->
                requireContext().saveUserIdToSharedPreferences(userId = user.id)
                val direction = AdminUserListFragmentDirections.toHome()
                findNavController().navigate(direction)
            }.apply {
                if (isAuthenticated)
                    setNeutralButton(R.string.view_user_as_admin) { _, _ ->
                        val direction = AdminUserListFragmentDirections.toUserDetails(user.id)
                        findNavController().navigate(direction)
                    }
            }
            .create()
            .show()
    }
}
