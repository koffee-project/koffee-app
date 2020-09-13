package eu.yeger.koffee.ui.home

import androidx.appcompat.app.AlertDialog
import eu.yeger.koffee.R
import eu.yeger.koffee.goToSharedActivity
import eu.yeger.koffee.goToUserSelection
import eu.yeger.koffee.ui.item.list.MainItemListFragment
import eu.yeger.koffee.utility.deleteUserIdFromSharedPreferences
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences

/**
 * [HomeFragment] for the single user mode.
 *
 * @property userId The id of the active user stored in the shared preferences.
 *
 * @author Jan Müller
 */
class MainHomeFragment : HomeFragment() {

    override val userId by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

    override fun getItemListFragment() = MainItemListFragment()

    /**
     * Removes the id of the active user from the shares preference and shows an info dialogue.
     */
    override fun onNotFound() {
        val message = when (userId) {
            null -> R.string.no_user_selected
            else -> R.string.active_user_deleted
        }
        requireContext().deleteUserIdFromSharedPreferences()
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.got_to_selection) { _, _ ->
                requireActivity().run {
                    goToUserSelection()
                    finish()
                }
            }
            .apply {
                if (userId === null) {
                    setNeutralButton(R.string.launch_shared_activity) { _, _ ->
                        requireActivity().goToSharedActivity()
                    }
                }
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
