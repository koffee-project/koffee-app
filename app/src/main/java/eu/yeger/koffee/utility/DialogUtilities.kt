package eu.yeger.koffee.utility

import android.app.Activity
import android.app.AlertDialog
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import eu.yeger.koffee.R

/**
 * Builds and shows a delete dialog using the given parameters.
 *
 * @param id The id of the entity to be deleted.
 * @param onConfirm Lambda which is called upon second confirmation.
 *
 * @author Jan Müller
 */
fun Fragment.showDeleteDialog(id: String, onConfirm: () -> Unit) {
    AlertDialog.Builder(requireContext())
        .setMessage(getString(R.string.delete_format, id))
        .setPositiveButton(R.string.delete) { _, _ ->
            showDeleteConfirmationDialog(id, onConfirm)
        }
        .setNegativeButton(R.string.cancel) { _, _ -> /*ignore*/ }
        .setCancelable(false)
        .create()
        .show()
}

private fun Fragment.showDeleteConfirmationDialog(id: String, onConfirm: () -> Unit) {
    AlertDialog.Builder(requireContext())
        .setMessage(getString(R.string.delete_confirmation_format, id))
        .setPositiveButton(R.string.delete) { _, _ ->
            onConfirm()
        }
        .setNegativeButton(R.string.cancel) { _, _ -> /*ignore*/ }
        .setCancelable(false)
        .create()
        .show()
}

/**
 * Hides the keyboard from the fragment.
 *
 * @receiver The fragment showing the keyboard.
 *
 * @author Jan Müller
 */
fun Fragment.hideKeyboard() {
    val imm: InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val token = requireView().rootView.windowToken
    imm.hideSoftInputFromWindow(token, 0)
}
