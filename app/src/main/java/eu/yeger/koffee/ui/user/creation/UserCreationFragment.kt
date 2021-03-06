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
import eu.yeger.koffee.utility.hideKeyboard
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the user creation screen.
 *
 * @author Jan Müller
 */
class UserCreationFragment : Fragment() {

    private val userCreationViewModel: UserCreationViewModel by viewModelFactories {
        val context = requireContext()
        UserCreationViewModel(
            adminRepository = AdminRepository(context),
            userRepository = UserRepository(context)
        )
    }

    /**
     * Inflates and initializes the layout.
     *
     * @param inflater Used for layout inflation.
     * @param container Unused.
     * @param savedInstanceState Unused.
     * @return The user creation view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userCreationViewModel.apply {
            observeAction(userCreatedAction) { userId ->
                hideKeyboard()
                requireActivity().showSnackbar(getString(R.string.user_creation_success))
                val direction = UserCreationFragmentDirections.toUserDetails(userId)
                findNavController().navigate(direction)
            }

            onAuthorizationException {
                hideKeyboard()
                requireActivity().showSnackbar(R.string.login_expired)
                val direction = UserCreationFragmentDirections.toSettings()
                direction.loginExpired = true
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentUserCreationBinding.inflate(inflater).apply {
            viewModel = userCreationViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
