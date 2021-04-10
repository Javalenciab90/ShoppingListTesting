package com.java90.shoppinglisttesting.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.java90.shoppinglisttesting.data.local.ShoppingDao
import com.java90.shoppinglisttesting.data.local.ShoppingDatabase
import com.java90.shoppinglisttesting.data.remote.PixibayAPI
import com.java90.shoppinglisttesting.other.Constants.BASE_URL
import com.java90.shoppinglisttesting.repository.ShoppingRepository
import com.java90.shoppinglisttesting.repository.ShoppingRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            ShoppingDatabase.DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideShoppingDao(database: ShoppingDatabase) : ShoppingDao {
        return database.shoppingDao()
    }

    @Singleton
    @Provides
    fun providePixibayAPI() : PixibayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixibayAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideShoppingRepositoryImp(dao: ShoppingDao, api: PixibayAPI) : ShoppingRepository {
        return ShoppingRepositoryImp(dao, api)
    }
}