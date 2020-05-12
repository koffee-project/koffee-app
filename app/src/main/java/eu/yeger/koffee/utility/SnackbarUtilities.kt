package eu.yeger.koffee.utility

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import eu.yeger.koffee.R
import eu.yeger.koffee.repository.RepositoryState

fun Activity.showRefreshResultSnackbar(
    repositoryState: RepositoryState,
    successText: Int,
    errorTextFormat: Int
) {
    val message = when (repositoryState) {
        // Disabled because displaying messages for success is uncommon
        // is RepositoryState.Done -> getString(successText)
        is RepositoryState.Error -> getString(errorTextFormat).format(
            repositoryState.exception.message ?: "Unknown"
        )
        else -> return // impossible
    }
    showSnackbar(message)
}

fun Activity.showSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(
        findViewById(R.id.container),
        message,
        length
    ).apply {
        anchorView = findViewById(R.id.nav_view)
    }.show()
}
