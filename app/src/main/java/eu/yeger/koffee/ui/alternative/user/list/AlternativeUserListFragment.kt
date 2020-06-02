package eu.yeger.koffee.ui.alternative.user.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.databinding.FragmentUserListBinding
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.UserViewHolder
import eu.yeger.koffee.ui.adapter.userListAdapter
import eu.yeger.koffee.ui.alternative.AlternativeActivity
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class AlternativeUserListFragment : Fragment() {

    private val userListViewModel: AlternativeUserListViewModel by viewModelFactories {
        AlternativeUserListViewModel(UserRepository(requireContext()))
    }

    private var userViewHolderFactory: UserViewHolder.Factory? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewHolderFactory = UserViewHolder.Factory(ProfileImageRepository(requireContext()))
        userListViewModel.apply {

            observeAction(userSelectedAction) { userId ->
                val direction = AlternativeUserListFragmentDirections.toAltUserDetails(userId)
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentUserListBinding.inflate(inflater).apply {
            isMainActivty = false
            viewModel = userListViewModel
            userRecyclerView.adapter = userListAdapter(userViewHolderFactory!!, OnClickListener { selectedUser ->
                userListViewModel.activateUserSelectedAction(selectedUser)
            })
            lifecycleOwner = viewLifecycleOwner
            alternativeActivityButton.setOnClickListener {
                requireActivity().run {
                    val intent = Intent(this, AlternativeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
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
