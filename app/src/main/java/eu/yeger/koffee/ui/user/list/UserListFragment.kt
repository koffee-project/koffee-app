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

/**
 * Abstract [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the user list screen.
 * Supports searching.
 *
 * @property userListViewModel The [UserListViewModel] used for accessing the user list.
 *
 * @author Jan Müller
 */
abstract class UserListFragment : Fragment() {

    protected abstract val userListViewModel: UserListViewModel

    private var userViewHolderFactory: UserViewHolder.Factory? = null

    /**
     * Called when the [UserListViewModel] is initialized.
     */
    protected abstract fun initializeViewModel()

    /**
     * Inflates and initializes the layout.
     *
     * @param inflater Used for layout inflation.
     * @param container Unused.
     * @param savedInstanceState Unused.
     * @return The user list view.
     */
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

    /**
     * Refreshes the [UserListViewModel].
     */
    override fun onResume() {
        userListViewModel.refreshUsers()
        super.onResume()
    }

    /**
     * Cancels the asynchronous loading of profile images.
     */
    override fun onPause() {
        super.onPause()
        userViewHolderFactory?.clear()
    }
}
