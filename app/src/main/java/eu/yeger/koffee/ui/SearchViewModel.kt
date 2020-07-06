package eu.yeger.koffee.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.utility.mediatedLiveData
import eu.yeger.koffee.utility.sourcedLiveData

/**
 * Abstract ViewModel that supports searching a PagedList [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData).
 *
 * @param T The type of the elements.
 * @property entries The [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) containing all searchable elements.
 * @property searchQuery Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the search query.
 * @property isBusy Indicates that a search is in progress.
 * @property filteredEntries The filtered elements.
 * @property onFilteredEntriesApplied Must be called after the filtered elements have changed and the changes have been applied.
 * @property hasResults Indicates that there are filtered elements.
 * @property hasNoResults Indicates that there are no filtered elements.
 *
 * @author Jan Müller
 */
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

    /**
     * Get the filteredElements for the given filter.
     *
     * @param filter The filter derived from the search query.
     * @return The filtered elements.
     */
    protected abstract fun getSource(filter: Filter): LiveData<PagedList<T>>
}
