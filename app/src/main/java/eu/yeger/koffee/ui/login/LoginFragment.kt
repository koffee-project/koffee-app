package eu.yeger.koffee.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentLoginBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.utility.showSnackbar

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.Factory(
            adminRepository = AdminRepository(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginViewModel.loginErrorAction.observe(viewLifecycleOwner, Observer { loginError ->
            loginError?.let {
                showSnackbar(loginError)
                loginViewModel.onLoginErrorActionHandled()
            }
        })

        loginViewModel.loginSuccessAction.observe(viewLifecycleOwner, Observer { loginSuccess ->
            if (loginSuccess) {
                showSnackbar(getString(R.string.login_success))
                val action = LoginFragmentDirections.actionNavigationLoginToNavigationAdmin()
                findNavController().navigate(action)
                loginViewModel.onLoginSuccessActionHandled()
            }
        })

        val binding = FragmentLoginBinding.inflate(inflater)
        binding.apply {
            viewModel = loginViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}
