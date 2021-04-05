package com.java90.shoppinglisttesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.java90.shoppinglisttesting.data.local.ShoppingItem
import com.java90.shoppinglisttesting.data.remote.responses.ImageResponse
import com.java90.shoppinglisttesting.other.Constants
import com.java90.shoppinglisttesting.other.Event
import com.java90.shoppinglisttesting.other.Resource
import com.java90.shoppinglisttesting.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Just use one common ViewModel to Simplify.
 */

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel(){

    val shoppingItems: LiveData<List<ShoppingItem>> = repository.observeAllShoppingItems()
    val totalPrice : LiveData<Float> = repository.observeTotalPrice()

    // To get all Images to choose the image for specific shopping item
    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images : LiveData<Event<Resource<ImageResponse>>> = _images

    // To set image on shopping item
    private val _currentImageUrl = MutableLiveData<String>()
    val currentImageUrl  : LiveData<String> = _currentImageUrl

    // To validate name, amount...to insert the new Item
    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus : LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun serCurrentImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteShoppingItem(shoppingItem)
        }
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            repository.insertShoppingItem(shoppingItem)
        }
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The fields must not be empty", null)))
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "The name of the item must not exceed ${Constants.MAX_NAME_LENGTH} characters",
                null)
            ))
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "The amount of the item must not exceed ${Constants.MAX_PRICE_LENGTH} characters",
                null)
            ))
            return
        }

        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error(
                "Please enter a valid amount",
                null)
            ))
            return
        }

        val shoppingItem = ShoppingItem(
            name,
            amount,
            priceString.toFloat(),
            _currentImageUrl.value ?: "" )

        insertShoppingItemIntoDb(shoppingItem)
        serCurrentImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) return
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }

}