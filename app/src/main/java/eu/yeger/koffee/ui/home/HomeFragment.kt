package eu.yeger.koffee.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentHomeBinding
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.RefundViewModel
import eu.yeger.koffee.ui.item.list.ItemListFragment
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

/**
 * Abstract [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the home screen.
 * Supports refunds.
 *
 * @property userId The id of the user this [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) is for.
 *
 * @author Jan Müller
 */
abstract class HomeFragment : Fragment() {

    protected abstract val userId: String?

    private val homeViewModel: HomeViewModel by viewModelFactories {
        val context = requireContext()
        HomeViewModel(
            userId!!,
            ProfileImageRepository(context),
            TransactionRepository(context),
            UserRepository(context)
        )
    }

    private val refundViewModel: RefundViewModel by viewModelFactories {
        val context = requireContext()
        RefundViewModel(
            userId = userId,
            itemRepository = ItemRepository(context),
            userRepository = UserRepository(context),
            transactionRepository = TransactionRepository(context)
        )
    }

    /**
     * Creates the [ItemListFragment] used by the home view.
     *
     * @return The [ItemListFragment] used by the home view.
     */
    protected abstract fun getItemListFragment(): ItemListFragment

    /**
     * Called when the user with the given id does no longer exist.
     */
    protected abstract fun onNotFound()

    /**
     * Inflates and initializes the layout.
     *
     * @param inflater Used for layout inflation.
     * @param container Unused.
     * @param savedInstanceState Unused.
     * @return The home view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (userId === null) {
            onNotFound()
            return null
        }

        homeViewModel.apply {
            observeAction(editProfileImageAction) { canDelete ->
                showModifyImageDialog(canDelete)
            }

            observeAction(userNotFoundAction) {
                onNotFound()
            }

            onErrorShowSnackbar()
        }

        return FragmentHomeBinding.inflate(inflater).apply {
            homeViewModel = this@HomeFragment.homeViewModel
            refundViewModel = this@HomeFragment.refundViewModel
            lifecycleOwner = viewLifecycleOwner
        }.root.also { setItemListFragment() }
    }

    /**
     * Refreshes the [HomeViewModel].
     */
    override fun onResume() {
        if (userId !== null) {
            homeViewModel.refreshUser()
        }

        super.onResume()
    }

    private fun setItemListFragment() {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .run {
                replace(R.id.item_list_fragment, getItemListFragment())
                commit()
            }
    }

    private fun showModifyImageDialog(canDelete: Boolean) {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.select_image_action)
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
            .compress(1024) // Limit size to 1MB
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
}
