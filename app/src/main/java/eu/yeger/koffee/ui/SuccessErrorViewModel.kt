package eu.yeger.koffee.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

abstract class SuccessErrorViewModel<T> : ViewModel() {

    protected val _successAction = MutableLiveData<T>()
    val successAction: LiveData<T> = _successAction

    private val _errorAction = MutableLiveData<String?>()
    val errorAction: LiveData<String?> = _errorAction

    protected val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _errorAction.postValue(exception.localizedMessage)
    }

    fun onErrorActionHandled() {
        viewModelScope.launch {
            _errorAction.value = null
        }
    }

    fun onSuccessActionHandled() {
        viewModelScope.launch {
            _successAction.value = null
        }
    }
}

fun <T> SuccessErrorViewModel<T>.onSuccess(fragment: Fragment, block: (T) -> Unit) {
    successAction.observe(fragment.viewLifecycleOwner, Observer {
        it?.let {
            block(it)
            onSuccessActionHandled()
        }
    })
}

fun SuccessErrorViewModel<*>.onError(fragment: Fragment, block: (String) -> Unit) {
    errorAction.observe(fragment.viewLifecycleOwner, Observer {
        it?.let {
            block(it)
            onErrorActionHandled()
        }
    })
}
