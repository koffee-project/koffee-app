package eu.yeger.koffee.ui.user_creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserCreationBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.showSnackbar

class UserCreationFragment : Fragment() {

    private val userCreationFragment: UserCreationViewModel by viewModels {
        val context = requireContext()
        UserCreationViewModel.Factory(
            adminRepository = AdminRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userCreationFragment.apply {
            successAction.observe(viewLifecycleOwner, Observer { userId ->
                userId?.let {
                    requireActivity().showSnackbar(getString(R.string.user_creation_success))
                    val action = UserCreationFragmentDirections.toUserDetails()
                    action.userId = userId
                    findNavController().navigate(action)
                    onSuccessActionHandled()
                }
            })

            errorAction.observe(viewLifecycleOwner, Observer { error ->
                error?.let {
                    requireActivity().showSnackbar(error)
                    onErrorActionHandled()
                }
            })
        }

        return FragmentUserCreationBinding.inflate(inflater).apply {
            viewModel = userCreationFragment
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
