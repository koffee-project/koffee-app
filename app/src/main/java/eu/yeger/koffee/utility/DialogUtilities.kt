package eu.yeger.koffee.utility

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import eu.yeger.koffee.R

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
