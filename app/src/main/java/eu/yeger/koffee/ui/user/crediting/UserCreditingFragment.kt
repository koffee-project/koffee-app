package eu.yeger.koffee.ui.user.crediting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserCreditingBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.user.editing.UserEditingFragmentArgs
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

class UserCreditingFragment : Fragment() {

    private val userCreditingViewModel: UserCreditingViewModel by viewModelFactories {
        val context = requireContext()
        UserCreditingViewModel(
            userId = UserEditingFragmentArgs.fromBundle(requireArguments()).userId,
            adminRepository = AdminRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userCreditingViewModel.apply {
            observeAction(userCreditedAction) { userId ->
                requireActivity().showSnackbar(getString(R.string.user_crediting_success))
                val direction = UserCreditingFragmentDirections.toUserDetails(userId)
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentUserCreditingBinding.inflate(inflater).apply {
            viewModel = userCreditingViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
