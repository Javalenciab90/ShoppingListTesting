package com.java90.shoppinglisttesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.java90.shoppinglisttesting.data.local.ShoppingItem
import com.java90.shoppinglisttesting.data.remote.responses.ImageResponse
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
    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ImageResponse>>>()
    val insertShoppingItemStatus : LiveData<Event<Resource<ImageResponse>>> = _insertShoppingItemStatus

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

    }

    fun searchForImage(imageQuery: String) {

    }

}