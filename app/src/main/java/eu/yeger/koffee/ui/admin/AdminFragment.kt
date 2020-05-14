package eu.yeger.koffee.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.databinding.FragmentAdminBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.utility.viewModelFactories

class AdminFragment : Fragment() {

    private val adminViewModel: AdminViewModel by viewModelFactories {
        AdminViewModel(
            adminRepository = AdminRepository(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        adminViewModel.apply {
            loginRequiredAction.observe(viewLifecycleOwner, Observer { loginRequired ->
                if (loginRequired) {
                    val action = AdminFragmentDirections.toLogin()
                    findNavController().navigate(action)
                    onLoginRequiredActionHandled()
                }
            })
        }

        return FragmentAdminBinding.inflate(inflater).apply {
            viewModel = adminViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
