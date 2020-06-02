package eu.yeger.koffee.ui.user.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.utility.observeAction

abstract class UserDetailsFragment : Fragment() {

    protected abstract val userDetailsViewModel: UserDetailsViewModel

    protected abstract fun initializeViewModel()

    protected abstract fun FragmentUserDetailsBinding.initializeBinding()

    protected abstract fun onNotFound()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userDetailsViewModel.apply {
            initializeViewModel()

            observeAction(userNotFoundAction) {
                onNotFound()
            }

            observeProfileImageActions()

            onErrorShowSnackbar()
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            initializeBinding()
            userDetailsViewModel = this@UserDetailsFragment.userDetailsViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        userDetailsViewModel.refreshUser()
        super.onResume()
    }
}
