package eu.yeger.koffee.ui.user.selection

import android.app.AlertDialog
import eu.yeger.koffee.R
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.goToMainActivity
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.user.list.UserListFragment
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.saveUserIdToSharedPreferences
import eu.yeger.koffee.utility.viewModelFactories

class UserSelectionFragment : UserListFragment() {

    override val userListViewModel: UserSelectionViewModel by viewModelFactories {
        UserSelectionViewModel(UserRepository(requireContext()))
    }

    override fun initializeViewModel() {
        userListViewModel.apply {
            observeAction(userSelectedAction) { selectedUser ->
                showUserSelectionDialog(selectedUser)
            }
        }
    }

    private fun showUserSelectionDialog(user: User) {
        val message = getString(R.string.set_active_user_format, user.name, user.id)
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.set_as_active_user) { _, _ ->
                requireContext().saveUserIdToSharedPreferences(userId = user.id)
                requireActivity().goToMainActivity()
            }
            .create()
            .show()
    }
}
