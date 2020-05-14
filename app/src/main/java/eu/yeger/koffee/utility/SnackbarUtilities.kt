package eu.yeger.koffee.utility

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import eu.yeger.koffee.R

fun Activity.showSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(
        findViewById(R.id.container),
        message,
        length
    ).apply {
        anchorView = findViewById(R.id.nav_view)
    }.show()
}
