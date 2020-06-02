package eu.yeger.koffee.ui.user.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.yeger.koffee.databinding.FragmentUserListBinding
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.UserViewHolder
import eu.yeger.koffee.ui.adapter.userListAdapter

abstract class UserListFragment : Fragment() {

    protected abstract val userListViewModel: UserListViewModel

    private var userViewHolderFactory: UserViewHolder.Factory? = null

    protected abstract fun initializeViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewHolderFactory = UserViewHolder.Factory(ProfileImageRepository(requireContext()))

        userListViewModel.apply {
            initializeViewModel()
            onErrorShowSnackbar()
        }

        return FragmentUserListBinding.inflate(inflater).apply {
            viewModel = userListViewModel
            userRecyclerView.adapter = userListAdapter(userViewHolderFactory!!, OnClickListener { selectedUser ->
                userListViewModel.activateUserSelectedAction(selectedUser)
            })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        userListViewModel.refreshUsers()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        userViewHolderFactory?.clear()
    }
}
