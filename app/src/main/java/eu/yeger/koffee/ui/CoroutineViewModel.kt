package eu.yeger.koffee.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.R
import eu.yeger.koffee.utility.hideKeyboard
import eu.yeger.koffee.utility.nullIfBlank
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Abstract ViewModel that supports automatic error handling for Coroutines.
 *
 * @author Jan Müller
 */
abstract class CoroutineViewModel : ViewModel() {
    private val errorAction = DataAction<Throwable>()

    private val unauthorizedAction = SimpleAction()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorAction.activateWith(throwable)
        if (throwable is HttpException && throwable.code() == 401) {
            unauthorizedAction.activate()
        }
    }

    private val defaultErrorFormatter: Fragment.(Throwable) -> String = { error ->
        when (error) {
            is HttpException -> error.response()?.errorBody()?.string().nullIfBlank()
                ?: error.message()
            is SocketTimeoutException -> getString(R.string.no_connection)
            is UnknownHostException -> getString(R.string.no_connection)
            else -> error.localizedMessage
        }.nullIfBlank() ?: getString(R.string.unknown_error)
    }

    /**
     * Executes a suspending lambda on the viewModelScope and handles all errors.
     *
     * @param block The suspending lambda that is safely executed.
     */
    protected fun onViewModelScope(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(context = exceptionHandler, block = block)

    /**
     * Observes the error [DataAction] of this [CoroutineViewModel] with the given lambda.
     *
     * @receiver The observing Fragment.
     * @param block Lambda that is executed when an exception occurs.
     */
    fun Fragment.onError(block: (Throwable) -> Unit) {
        observeAction(errorAction, block)
    }

    /**
     * Observes the error [DataAction] of this [CoroutineViewModel] and shows the formatted error message as a snackbar.
     * Also hides any active keyboards.
     *
     * @receiver The observing Fragment.
     * @param block Custom error formatter.
     */
    fun Fragment.onErrorShowSnackbar(
        block: (Fragment.(Throwable) -> String?)? = null
    ) {
        onError {
            Timber.e(it)
            hideKeyboard()
            val message = block?.invoke(this, it).nullIfBlank() ?: defaultErrorFormatter(it)
            requireActivity().showSnackbar(message)
        }
    }

    /**
     * Observes the authorization error [DataAction] of this [CoroutineViewModel] with the given lambda.
     *
     * @param block Lambda that is executed when an authorization exception occurs.
     */
    fun Fragment.onAuthorizationException(block: () -> Unit) {
        observeAction(unauthorizedAction, block)
    }
}
