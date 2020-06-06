package eu.yeger.koffee.ui.home


import androidx.appcompat.app.AlertDialog
import eu.yeger.koffee.R
import eu.yeger.koffee.utility.deleteUserIdFromSharedPreferences

class SharedHomeFragment : HomeFragment() {

    override val userId by lazy {
        SharedHomeFragmentArgs.fromBundle(requireArguments()).userId
    }

    override fun onNotFound() {
        requireContext().deleteUserIdFromSharedPreferences()
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.selected_user_deleted)
            .setPositiveButton(R.string.got_to_selection) { _, _ ->
                TODO()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
