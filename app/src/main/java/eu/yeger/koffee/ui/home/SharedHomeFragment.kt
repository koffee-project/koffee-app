package eu.yeger.koffee.ui.home

import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.ui.item.list.SharedItemListFragment
import eu.yeger.koffee.utility.deleteUserIdFromSharedPreferences

/**
 * [HomeFragment] for the multi user mode.
 *
 * @property userId The id of the user from the arguments.
 *
 * @author Jan Müller
 */
class SharedHomeFragment : HomeFragment() {

    override val userId by lazy {
        SharedHomeFragmentArgs.fromBundle(requireArguments()).userId
    }

    override fun getItemListFragment() = SharedItemListFragment(userId)

    /**
     * Shows an info dialogue.
     */
    override fun onNotFound() {
        requireContext().deleteUserIdFromSharedPreferences()
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.selected_user_deleted)
            .setPositiveButton(R.string.got_to_selection) { _, _ ->
                val direction = SharedHomeFragmentDirections.toSharedUserList()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
