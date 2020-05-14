package eu.yeger.koffee.ui.user.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserListBinding
import eu.yeger.koffee.domain.UserEntry
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserEntryRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.UserEntryListAdapter
import eu.yeger.koffee.ui.onErrorShowSnackbar
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences
import eu.yeger.koffee.utility.observe
import eu.yeger.koffee.utility.saveUserIdToSharedPreferences
import eu.yeger.koffee.utility.viewModelFactories

class UserListFragment : Fragment() {

    private val userListViewModel: UserListViewModel by viewModelFactories {
        val context = requireContext()
        UserListViewModel(
            adminRepository = AdminRepository(context),
            userEntryRepository = UserEntryRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userListViewModel.apply {
            observe(createUserAction) { createUser ->
                if (createUser) {
                    val action = UserListFragmentDirections.toUserCreation()
                    findNavController().navigate(action)
                    onCreateUserActionHandled()
                }
            }

            observe(userEntrySelectedAction) { pair ->
                pair?.let {
                    val (isAuthenticated, userEntry) = pair
                    val canView =
                        isAuthenticated && requireContext().getUserIdFromSharedPreferences() != userEntry.id
                    showUserSelectionDialog(userEntry, canView)
                    onUserEntrySelectedActionHandled()
                }
            }

            onErrorShowSnackbar(this@UserListFragment) { error ->
                getString(R.string.user_refresh_error_format, error.localizedMessage)
            }
        }

        return FragmentUserListBinding.inflate(inflater).apply {
            viewModel = userListViewModel
            searchResultRecyclerView.adapter =
                UserEntryListAdapter(OnClickListener { selectedUserEntry ->
                    userListViewModel.triggerUserEntrySelectedAction(selectedUserEntry)
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun showUserSelectionDialog(userEntry: UserEntry, canView: Boolean) {
        val message = getString(R.string.set_active_user_format, userEntry.name, userEntry.id)
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.set_as_active_user) { _, _ ->
                setActiveUser(userEntry)
            }.apply {
                if (canView)
                    setNeutralButton(R.string.view_user_as_admin) { _, _ ->
                        val action = UserListFragmentDirections.toUserDetails(userEntry.id)
                        findNavController().navigate(action)
                    }
            }
            .create()
            .show()
    }

    private fun setActiveUser(userEntry: UserEntry) {
        requireContext().saveUserIdToSharedPreferences(userId = userEntry.id)
        val action = UserListFragmentDirections.toHome()
        findNavController().navigate(action)
    }
}
