package eu.yeger.koffee.ui.user.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class SharedUserListFragment : UserListFragment() {

    override val userListViewModel: SharedUserListViewModel by viewModelFactories {
        SharedUserListViewModel(
            UserRepository(requireContext())
        )
    }

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
