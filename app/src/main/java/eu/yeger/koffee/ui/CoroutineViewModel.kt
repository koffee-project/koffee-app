package eu.yeger.koffee.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.R
import eu.yeger.koffee.utility.nullIfBlank
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import java.net.UnknownHostException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

abstract class CoroutineViewModel : ViewModel() {
    private val errorAction = Action<Throwable>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorAction.activateWith(throwable)
    }

    private val defaultErrorFormatter: Fragment.(Throwable) -> String = { error ->
        when (error) {
            is UnknownHostException -> getString(R.string.no_connection)
            is HttpException -> error.response()?.errorBody()?.string()
            else -> error.localizedMessage
        }.nullIfBlank() ?: getString(R.string.unknown_error)
    }

    protected fun onViewModelScope(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(context = exceptionHandler, block = block)

    fun Fragment.onError(block: (Throwable) -> Unit) {
        observeAction(errorAction, block)
    }

    fun Fragment.onErrorShowSnackbar(
        block: (Fragment.(Throwable) -> String?)? = null
    ) {
        onError {
            val message = block?.invoke(this, it).nullIfBlank() ?: defaultErrorFormatter(it)
            requireActivity().showSnackbar(message)
        }
    }
}
