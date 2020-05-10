package eu.yeger.koffee.ui.user_creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import eu.yeger.koffee.databinding.FragmentUserCreationBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository

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
        return FragmentUserCreationBinding.inflate(inflater).apply {
            viewModel = userCreationFragment
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
