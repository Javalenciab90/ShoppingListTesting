package com.java90.shoppinglisttesting.repository

import androidx.lifecycle.LiveData
import com.java90.shoppinglisttesting.data.local.ShoppingDao
import com.java90.shoppinglisttesting.data.local.ShoppingItem
import com.java90.shoppinglisttesting.data.remote.PixibayAPI
import com.java90.shoppinglisttesting.data.remote.responses.ImageResponse
import com.java90.shoppinglisttesting.other.Resource
import javax.inject.Inject
import kotlin.Exception

class ShoppingRepositoryImp @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixibayAPI: PixibayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixibayAPI.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            }else {
                Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection ", null)
        }
    }
}