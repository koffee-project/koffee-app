package eu.yeger.koffee.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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