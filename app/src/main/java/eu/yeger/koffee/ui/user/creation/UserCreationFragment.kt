package eu.yeger.koffee.ui.user.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserCreationBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.onError
import eu.yeger.koffee.ui.onSuccess
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

class UserCreationFragment : Fragment() {

    private val userCreationFragment: UserCreationViewModel by viewModelFactories {
        val context = requireContext()
        UserCreationViewModel(
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
            onSuccess(this@UserCreationFragment) { userId ->
                requireActivity().showSnackbar(getString(R.string.user_creation_success))
                val action = UserCreationFragmentDirections.toUserDetails(userId)
                findNavController().navigate(action)
            }

            onError(this@UserCreationFragment) { error ->
                requireActivity().showSnackbar(error)
            }
        }

        return FragmentUserCreationBinding.inflate(inflater).apply {
            viewModel = userCreationFragment
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
