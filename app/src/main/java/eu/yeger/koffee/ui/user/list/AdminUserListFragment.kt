package eu.yeger.koffee.ui.user.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [UserListFragment] for user management.
 * Supports searching.
 *
 * @property userListViewModel The [AdminUserListViewModel] used for accessing the item list.
 *
 * @author Jan Müller
 */
class AdminUserListFragment : UserListFragment() {

    override val userListViewModel: AdminUserListViewModel by viewModelFactories {
        val context = requireContext()
        AdminUserListViewModel(
            adminRepository = AdminRepository(context),
            userRepository = UserRepository(context)
        )
    }

    /**
     * Initializes the [AdminUserListViewModel].
     */
    override fun initializeViewModel() {
        userListViewModel.apply {
            observeAction(createUserAction) {
                val direction = AdminUserListFragmentDirections.toUserCreation()
                findNavController().navigate(direction)
            }

            observeAction(userSelectedAction) { user ->
                val direction = AdminUserListFragmentDirections.toUserDetails(user.id)
                findNavController().navigate(direction)
            }
        }
    }
}
