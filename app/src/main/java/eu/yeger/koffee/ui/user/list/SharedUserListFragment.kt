package eu.yeger.koffee.ui.user.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [UserListFragment] for the multi user mode.
 * Supports searching.
 *
 * @property userListViewModel The [UserListViewModel] used for accessing the user list.
 *
 * @author Jan Müller
 */
class SharedUserListFragment : UserListFragment() {

    override val userListViewModel: SharedUserListViewModel by viewModelFactories {
        SharedUserListViewModel(
            UserRepository(requireContext())
        )
    }

    /**
     * Initializes the [SharedUserListViewModel].
     */
    override fun initializeViewModel() {
        userListViewModel.apply {
            observeAction(userSelectedAction) { userId ->
                val direction =
                    SharedUserListFragmentDirections.toSharedUserDetails(
                        userId
                    )
                findNavController().navigate(direction)
            }
        }
    }
}
