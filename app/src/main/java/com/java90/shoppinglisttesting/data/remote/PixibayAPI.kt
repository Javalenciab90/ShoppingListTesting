package com.java90.shoppinglisttesting.data.remote

import com.java90.shoppinglisttesting.BuildConfig
import com.java90.shoppinglisttesting.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixibayAPI {

    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ) : Response<ImageResponse>
}