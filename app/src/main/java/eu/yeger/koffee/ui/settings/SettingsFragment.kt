package eu.yeger.koffee.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentSettingsBinding
import eu.yeger.koffee.goToSharedActivity
import eu.yeger.koffee.goToUserSelection
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the settings screen.
 *
 * @author Jan Müller
 */
class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModelFactories {
        SettingsViewModel(
            loginExpired = SettingsFragmentArgs.fromBundle(requireArguments()).loginExpired,
            adminRepository = AdminRepository(requireContext())
        )
    }

    /**
     * Inflates and initializes the layout.
     *
     * @param inflater Used for layout inflation.
     * @param container Unused.
     * @param savedInstanceState Unused.
     * @return The login view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        settingsViewModel.apply {
            observeAction(loginExpiredAction) {
                requireActivity().showSnackbar(R.string.login_expired)
            }

            observeAction(selectUserAction) {
                requireActivity().goToUserSelection()
            }

            observeAction(launchSharedActivityAction) {
                requireActivity().goToSharedActivity()
            }

            observeAction(loginAction) {
                val direction = SettingsFragmentDirections.toLogin()
                findNavController().navigate(direction)
            }

            observeAction(manageItemsAction) {
                val direction = SettingsFragmentDirections.toAdminItemList()
                findNavController().navigate(direction)
            }

            observeAction(manageUsersAction) {
                val direction = SettingsFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentSettingsBinding.inflate(inflater).apply {
            settingsViewModel = this@SettingsFragment.settingsViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
