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

/**
 * Abstract [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the user details screen.
 * Supports refunds.
 *
 * @property userId The id of the user this [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) is for.
 * @property userDetailsViewModel The [UserDetailsViewModel] used for accessing user information.
 *
 * @author Jan Müller
 */
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

    /**
     * Called when the [UserDetailsViewModel] is initialized.
     */
    protected abstract fun initializeViewModel()

    /**
     * Called when the view binding is initialized.
     */
    protected abstract fun FragmentUserDetailsBinding.initializeBinding()

    /**
     * Called when the user with the given id does no longer exist.
     */
    protected abstract fun onNotFound()

    /**
     * Inflates and initializes the layout.
     *
     * @param inflater Used for layout inflation.
     * @param container Unused.
     * @param savedInstanceState Unused.
     * @return The user details view.
     */
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

    /**
     * Refreshes the [UserDetailsViewModel].
     */
    override fun onResume() {
        userDetailsViewModel.refreshUser()
        super.onResume()
    }
}
