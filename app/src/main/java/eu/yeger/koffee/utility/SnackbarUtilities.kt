package eu.yeger.koffee.utility

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import eu.yeger.koffee.R

/**
 * Shows a message using the unified snackbar layout.
 *
 * @param messageId The resource id of the snackbar's message.
 * @param length How long the snackbar will be shown. Defaults to short.
 *
 * @author Jan Müller
 */
fun Activity.showSnackbar(messageId: Int, length: Int = Snackbar.LENGTH_SHORT) {
    showSnackbar(getString(messageId), length)
}

/**
 * Shows a message using the unified snackbar layout.
 *
 * @param message The message of the snackbar.
 * @param length How long the snackbar will be shown. Defaults to short.
 *
 * @author Jan Müller
 */
fun Activity.showSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(
        findViewById(R.id.container),
        message,
        length
    ).apply {
        anchorView = findViewById(R.id.nav_view)
    }.show()
}
