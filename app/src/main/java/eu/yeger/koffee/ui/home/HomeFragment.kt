package eu.yeger.koffee.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import eu.yeger.koffee.AlternativeActivity
import eu.yeger.koffee.R
import eu.yeger.koffee.UserSelectionActivity
import eu.yeger.koffee.databinding.FragmentHomeBinding
import eu.yeger.koffee.goToUserSelection
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.RefundViewModel
import eu.yeger.koffee.utility.*

class HomeFragment : Fragment() {

    private val userId by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

    private val homeViewModel: HomeViewModel by viewModelFactories {
        val context = requireContext()
        HomeViewModel(
            userId!!,
            UserRepository(context),
            ProfileImageRepository(context)
        )
    }

    private val refundViewModel: RefundViewModel by viewModelFactories {
        val context = requireContext()
        RefundViewModel(
            userId = userId,
            userRepository = UserRepository(context),
            transactionRepository = TransactionRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (userId == null) {
            onNotFound()
            return null
        }

        homeViewModel.apply {
            observeAction(editProfileImageAction) { canDelete ->
                showModifyImageDialog(canDelete)
            }

            onErrorShowSnackbar()
        }

        return FragmentHomeBinding.inflate(inflater).apply {
            homeViewModel = this@HomeFragment.homeViewModel
            refundViewModel = this@HomeFragment.refundViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun showModifyImageDialog(canDelete: Boolean) {
        AlertDialog.Builder(requireContext())
            .setMessage("Select an action") // TODO change
            .setPositiveButton(R.string.edit) { _, _ ->
                showImageSelectionDialog()
            }
            .apply {
                if (canDelete) {
                    setNeutralButton(R.string.delete) { _, _ -> homeViewModel.deleteProfileImage() }
                }
            }
            .create()
            .show()
    }

    private fun showImageSelectionDialog() {
        ImagePicker.with(this)
            .compress(8 * 1024) // Limit size to 8MB
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val image = ImagePicker.getFile(data)!!
                        homeViewModel.uploadProfileImage(image)
                    }
                    ImagePicker.RESULT_ERROR -> requireActivity().showSnackbar(
                        ImagePicker.getError(data)
                    )
                }
            }
    }

    private fun onNotFound() {
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
            .setCancelable(false)
            .create()
            .show()
    }
}
