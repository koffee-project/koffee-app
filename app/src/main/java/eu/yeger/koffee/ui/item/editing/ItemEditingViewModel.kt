package eu.yeger.koffee.ui.item.editing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SuccessErrorViewModel
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

class ItemEditingViewModel(
    val itemId: String,
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository
) : SuccessErrorViewModel<String>() {

    val itemName = MutableLiveData("")

    val itemPrice = MutableLiveData("")

    val itemAmount = MutableLiveData("")

    // TODO verify price
    val canUpdateItem = sourcedLiveData(itemName, itemPrice, itemAmount) {
        itemName.value.isNullOrBlank().not() &&
                (itemAmount.value.isNullOrBlank() || itemAmount.value?.toIntOrNull() ?: -1 >= 0)
    }

    init {
        viewModelScope.launch {
            itemRepository.getItemById(itemId)?.run {
                itemName.value = name
                itemPrice.value = price.toString()
                itemAmount.value = amount?.toString() ?: ""
            }
        }
    }

    // TODO catch exceptions
    fun updateItem() {
        viewModelScope.launch(exceptionHandler) {
            val jwt = adminRepository.getJWT()!!
            itemRepository.updateItem(
                itemId = itemId,
                itemName = itemName.value!!,
                itemPrice = itemPrice.value!!.toDouble(),
                itemAmount = itemAmount.value?.toIntOrNull(),
                jwt = jwt
            )
            _successAction.value = itemId
        }
    }
}
