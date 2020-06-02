package eu.yeger.koffee.ui.user.details

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import eu.yeger.koffee.R

abstract class UserDetailsFragment : Fragment() {

    protected abstract val userDetailsViewModel: UserDetailsViewModel

    override fun onResume() {
        userDetailsViewModel.refreshUser()
        super.onResume()
    }

    protected fun showUserNotFoundDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.user_not_found)
            .setPositiveButton(R.string.go_back) { _, _ ->
                onNotFoundConfirmed()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    protected abstract fun onNotFoundConfirmed()
}
