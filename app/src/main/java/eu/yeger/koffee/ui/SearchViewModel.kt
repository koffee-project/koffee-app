package eu.yeger.koffee.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.utility.mediatedLiveData
import eu.yeger.koffee.utility.sourcedLiveData

abstract class SearchViewModel<T>(private val entries: LiveData<PagedList<T>>) : CoroutineViewModel() {

    val searchQuery = MutableLiveData<String>()

    private val _isBusy: MutableLiveData<Boolean> = mediatedLiveData {
        addSource(entries) { values: List<T>? ->
            value = values?.size ?: 0 == 0 || value ?: false
        }
        value = true
    }
    val isBusy: LiveData<Boolean> = _isBusy

    val filteredEntries = sourcedLiveData(entries, searchQuery) {
        Filter(query = searchQuery.value ?: "")
    }.switchMap { filter ->
        _isBusy.value = true
        getSource(filter)
    }
    val onFilteredEntriesApplied = { _isBusy.postValue(false) }

    val hasResults: LiveData<Boolean> = sourcedLiveData(filteredEntries) {
        filteredEntries.value?.size ?: 0 > 0
    }
    val hasNoResults: LiveData<Boolean> = sourcedLiveData(filteredEntries, isBusy) {
        filteredEntries.value?.size == 0 && !(isBusy.value ?: false)
    }

    protected abstract fun getSource(filter: Filter): LiveData<PagedList<T>>
}
