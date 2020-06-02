package eu.yeger.koffee.ui.alternative.user.details

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.RefundViewModel
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.ui.user.details.UserDetailsFragment
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

class AlternativeUserDetailsFragment : UserDetailsFragment() {

    private val userId by lazy {
        AlternativeUserDetailsFragmentArgs.fromBundle(requireArguments()).userId
    }

    override val userDetailsViewModel: AlternativeUserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        AlternativeUserDetailsViewModel(
            userId = userId,
            profileImageRepository = ProfileImageRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    private val refundViewModel: RefundViewModel by viewModelFactories {
        val context = requireContext()
        RefundViewModel(
            userId = userId,
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userDetailsViewModel.apply {
            observeAction(userNotFoundAction) {
                showUserNotFoundDialog()
            }

            observeAction(showItemsAction) {
                val direction = AlternativeUserDetailsFragmentDirections.toAltItemList()
                findNavController().navigate(direction)
            }

            observeAction(editProfileImageAction) {
                ImagePicker.with(this@AlternativeUserDetailsFragment)
                    .compress(8 * 1024) // Limit size to 8MB
                    .start { resultCode, data ->
                        when (resultCode) {
                            Activity.RESULT_OK -> {
                                val image = ImagePicker.getFile(data)!!
                                userDetailsViewModel.uploadProfileImage(image)
                            }
                            ImagePicker.RESULT_ERROR -> requireActivity().showSnackbar(ImagePicker.getError(data))
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
            userDetailsViewModel = this@AlternativeUserDetailsFragment.userDetailsViewModel
            refundViewModel = this@AlternativeUserDetailsFragment.refundViewModel
            transactionRecyclerView.adapter =
                transactionListAdapter(OnClickListener { selectedTransaction ->
                    when (selectedTransaction) {
                        is Transaction.Purchase -> selectedTransaction.itemId
                        is Transaction.Refund -> selectedTransaction.itemId
                        else -> null
                    }?.let { itemId ->
                        val direction =
                            AlternativeUserDetailsFragmentDirections.toAltItemDetails(itemId, userId)
                        findNavController().navigate(direction)
                    }
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onNotFoundConfirmed() {
        val direction = AlternativeUserDetailsFragmentDirections.toAltUserList()
        findNavController().navigate(direction)
    }
}
