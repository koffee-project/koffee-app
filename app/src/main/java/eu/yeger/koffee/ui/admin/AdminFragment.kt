package eu.yeger.koffee.ui.admin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.databinding.FragmentAdminBinding
import eu.yeger.koffee.repository.AdminRepository

class AdminFragment : Fragment() {

    private val adminViewModel: AdminViewModel by viewModels {
        AdminViewModel.Factory(
            adminRepository = AdminRepository(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        adminViewModel.loginRequiredAction.observe(viewLifecycleOwner, Observer { loginRequired ->
            if (loginRequired) {
                val action = AdminFragmentDirections.actionNavigationAdminToLoginFragment()
                findNavController().navigate(action)
                adminViewModel.onLoginRequiredActionHandled()
            }
        })

        val binding = FragmentAdminBinding.inflate(inflater)
        binding.apply {
            viewModel = adminViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}
