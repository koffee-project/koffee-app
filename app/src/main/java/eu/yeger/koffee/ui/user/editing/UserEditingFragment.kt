package eu.yeger.koffee.ui.user.editing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserEditingBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.hideKeyboard
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the user editing screen.
 *
 * @author Jan Müller
 */
class UserEditingFragment : Fragment() {

    private val userEditingViewModel: UserEditingViewModel by viewModelFactories {
        val context = requireContext()
        UserEditingViewModel(
            userId = UserEditingFragmentArgs.fromBundle(requireArguments()).userId,
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
     * @return The user editing view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userEditingViewModel.apply {
            observeAction(userUpdatedAction) { userId ->
                hideKeyboard()
                requireActivity().showSnackbar(getString(R.string.user_editing_success))
                val direction = UserEditingFragmentDirections.toUserDetails(userId)
                findNavController().navigate(direction)
            }

            onAuthorizationException {
                hideKeyboard()
                requireActivity().showSnackbar(R.string.login_expired)
                val direction = UserEditingFragmentDirections.toSettings()
                direction.loginExpired = true
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentUserEditingBinding.inflate(inflater).apply {
            viewModel = userEditingViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
