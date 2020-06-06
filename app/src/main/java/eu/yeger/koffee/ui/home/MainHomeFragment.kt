package eu.yeger.koffee.ui.home

import androidx.appcompat.app.AlertDialog
import eu.yeger.koffee.R
import eu.yeger.koffee.goToSharedActivity
import eu.yeger.koffee.goToUserSelection
import eu.yeger.koffee.ui.item.list.MainItemListFragment
import eu.yeger.koffee.utility.deleteUserIdFromSharedPreferences
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences

class MainHomeFragment : HomeFragment() {

    override val userId by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

    override fun getItemListFragment() = MainItemListFragment()

    override fun onNotFound() {
        val message = when (userId) {
            null -> R.string.no_user_selected
            else -> R.string.active_user_deleted
        }
        requireContext().deleteUserIdFromSharedPreferences()
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.got_to_selection) { _, _ ->
                requireActivity().goToUserSelection()
            }
            .apply {
                if (userId === null) {
                    setNeutralButton(R.string.launch_alternative_activity) { _, _ ->
                        requireActivity().goToSharedActivity()
                    }
                }
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
