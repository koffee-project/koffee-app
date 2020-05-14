package eu.yeger.koffee.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.R
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class CoroutineViewModel : ViewModel() {
    private val errorAction = Action<Throwable?>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorAction.activateWith(throwable)
    }

    protected fun onViewModelScope(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(context = exceptionHandler, block = block)

    fun Fragment.onError(block: (Throwable) -> Unit) {
        observeAction(errorAction, block)
    }

    fun Fragment.onErrorShowSnackbar(
        block: (Throwable) -> String = {
            it.localizedMessage ?: getString(R.string.unknown_error)
        }
    ) {
        onError {
            requireActivity().showSnackbar(block(it))
        }
    }
}
