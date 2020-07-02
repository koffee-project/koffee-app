package eu.yeger.koffee.ui.home

import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.ui.item.list.SharedItemListFragment
import eu.yeger.koffee.utility.deleteUserIdFromSharedPreferences

class SharedHomeFragment : HomeFragment() {

    override val userId by lazy {
        SharedHomeFragmentArgs.fromBundle(requireArguments()).userId
    }

    override fun getItemListFragment() = SharedItemListFragment(userId)

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
