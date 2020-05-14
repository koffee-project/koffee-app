package eu.yeger.koffee.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

abstract class SuccessViewModel<T> : CoroutineViewModel() {
    private val _successAction = MutableLiveData<T>()
    val successAction: LiveData<T> = _successAction

    protected fun setSuccessResult(result: T) = _successAction.postValue(result)

    fun onSuccessActionHandled() = _successAction.postValue(null)
}

fun <T> SuccessViewModel<T>.onSuccess(fragment: Fragment, block: (T) -> Unit) {
    successAction.observe(fragment.viewLifecycleOwner, Observer {
        it?.let {
            block(it)
            onSuccessActionHandled()
        }
    })
}
