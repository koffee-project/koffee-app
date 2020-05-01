package eu.yeger.koffee.utility

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import eu.yeger.koffee.repository.RepositoryState

fun Fragment.showRefreshResultSnackbar(
    repositoryState: RepositoryState,
    successText: Int,
    errorTextFormat: Int
) {
    val message = when (repositoryState) {
        is RepositoryState.Done -> getString(successText)
        is RepositoryState.Error -> getString(errorTextFormat).format(
            repositoryState.exception.message ?: "Unknown"
        )
        else -> return // impossible
    }
    Snackbar.make(
        requireView(),
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}
