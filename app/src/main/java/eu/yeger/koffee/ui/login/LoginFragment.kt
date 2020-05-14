package eu.yeger.koffee.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentLoginBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.onErrorShowSnackbar
import eu.yeger.koffee.ui.onSuccess
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModelFactories {
        LoginViewModel(adminRepository = AdminRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginViewModel.apply {
            onSuccess(this@LoginFragment) { loginSuccessful ->
                if (loginSuccessful) {
                    requireActivity().showSnackbar(getString(R.string.login_success))
                    val action = LoginFragmentDirections.toAdmin()
                    findNavController().navigate(action)
                }
            }

            onErrorShowSnackbar(this@LoginFragment)
        }

        return FragmentLoginBinding.inflate(inflater).apply {
            viewModel = loginViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
