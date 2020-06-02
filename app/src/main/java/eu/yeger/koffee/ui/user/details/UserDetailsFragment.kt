package eu.yeger.koffee.ui.user.details

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar

abstract class UserDetailsFragment : Fragment() {

    protected abstract val userDetailsViewModel: UserDetailsViewModel

    protected abstract fun initializeViewModel()

    protected abstract fun FragmentUserDetailsBinding.initializeBinding()

    protected abstract fun onNotFoundConfirmed()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userDetailsViewModel.apply {
            initializeViewModel()

            observeAction(userNotFoundAction) {
                showUserNotFoundDialog()
            }

            observeAction(editProfileImageAction) {
                ImagePicker.with(this@UserDetailsFragment)
                    .compress(8 * 1024) // Limit size to 8MB
                    .start { resultCode, data ->
                        when (resultCode) {
                            Activity.RESULT_OK -> {
                                val image = ImagePicker.getFile(data)!!
                                userDetailsViewModel.uploadProfileImage(image)
                            }
                            ImagePicker.RESULT_ERROR -> requireActivity().showSnackbar(
                                ImagePicker.getError(data)
                            )
                        }
                    }
            }

            observeAction(deleteProfileImageAction) {
                AlertDialog.Builder(requireContext())
                    .setMessage(R.string.delete_profile_image_confirmation)
                    .setPositiveButton(R.string.delete) { _, _ ->
                        userDetailsViewModel.deleteProfileImage()
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> /*ignore*/ }
                    .create()
                    .show()
            }

            onErrorShowSnackbar()
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            initializeBinding()
            userDetailsViewModel = this@UserDetailsFragment.userDetailsViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        userDetailsViewModel.refreshUser()
        super.onResume()
    }

    private fun showUserNotFoundDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.user_not_found)
            .setPositiveButton(R.string.go_back) { _, _ ->
                onNotFoundConfirmed()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
