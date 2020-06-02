package eu.yeger.koffee.ui.user.details

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R

abstract class BaseUserDetailsFragment : Fragment() {

    protected abstract val userDetailsViewModel: BaseUserDetailsViewModel

    override fun onResume() {
        userDetailsViewModel.refreshUser()
        super.onResume()
    }

    protected fun showUserNotFoundDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.user_not_found)
            .setPositiveButton(R.string.go_back) { _, _ ->
                val direction = UserDetailsFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
