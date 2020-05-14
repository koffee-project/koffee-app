package eu.yeger.koffee.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import eu.yeger.koffee.R
import eu.yeger.koffee.utility.showSnackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class CoroutineViewModel : ViewModel() {
    private val _errorAction = MutableLiveData<Throwable?>()
    val errorAction: LiveData<Throwable?> = _errorAction

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorAction.postValue(throwable)
    }

    protected fun onViewModelScope(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(context = exceptionHandler, block = block)

    fun onErrorActionHandled() = _errorAction.postValue(null)
}

fun CoroutineViewModel.onError(fragment: Fragment, block: (Throwable) -> Unit) {
    errorAction.observe(fragment.viewLifecycleOwner, Observer {
        it?.let {
            block(it)
            onErrorActionHandled()
        }
    })
}

fun CoroutineViewModel.onErrorShowSnackbar(
    fragment: Fragment,
    block: (Throwable) -> String = { it.localizedMessage ?: fragment.getString(R.string.unknown_error) }
) {
    onError(fragment) {
        fragment.requireActivity().showSnackbar(block(it))
    }
}
