package eu.yeger.koffee.ui.user.list

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class AlternativeUserListFragment : UserListFragment() {

    override val userListViewModel: AlternativeUserListViewModel by viewModelFactories {
        AlternativeUserListViewModel(
            UserRepository(requireContext())
        )
    }

    override fun initializeViewModel() {
        userListViewModel.apply {
            observeAction(userSelectedAction) { userId ->
                val direction =
                    AlternativeUserListFragmentDirections.toAltUserDetails(
                        userId
                    )
                findNavController().navigate(direction)
            }
        }
    }
}
