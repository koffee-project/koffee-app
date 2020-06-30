package eu.yeger.koffee.ui.user.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.RefundViewModel
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

abstract class UserDetailsFragment : Fragment() {

    protected abstract val userId: String?

    private val refundViewModel: RefundViewModel by viewModelFactories {
        val context = requireContext()
        RefundViewModel(
            userId = userId,
            itemRepository = ItemRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

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
            observeAction(userNotFoundAction) { onNotFound() }
            observeProfileImageActions()
            onErrorShowSnackbar()
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            initializeBinding()
            userDetailsViewModel = this@UserDetailsFragment.userDetailsViewModel
            refundViewModel = this@UserDetailsFragment.refundViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        userDetailsViewModel.refreshUser()
        super.onResume()
    }
}
