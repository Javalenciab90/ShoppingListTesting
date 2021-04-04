package com.java90.shoppinglisttesting.repository

import androidx.lifecycle.LiveData
import com.java90.shoppinglisttesting.data.local.ShoppingItem
import com.java90.shoppinglisttesting.data.remote.responses.ImageResponse
import com.java90.shoppinglisttesting.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems() : LiveData<List<ShoppingItem>>

    fun observeTotalPrice() : LiveData<Float>

    suspend fun searchForImage(imageQuery: String) : Resource<ImageResponse>

}