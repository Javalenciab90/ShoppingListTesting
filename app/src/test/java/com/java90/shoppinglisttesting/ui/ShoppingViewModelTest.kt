package com.java90.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.java90.shoppinglisttesting.MainCoroutineRule
import com.java90.shoppinglisttesting.getOrAwaitValueTest
import com.java90.shoppinglisttesting.other.Constants
import com.java90.shoppinglisttesting.other.Status
import com.java90.shoppinglisttesting.repository.FakeShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `given shopping item with valid input, returns success`() {
        // Given
        viewModel.insertShoppingItem("name", "50", "25.0")

        // When
        val valueStatus = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        // Then
        assertThat(valueStatus.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `given shopping item with empty field, returns error`() {
        // Given
        viewModel.insertShoppingItem("name", "", "25.0")

        // When
        val valueStatus = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        // Then
        assertThat(valueStatus.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `given shopping item with long name, returns error`() {
        // Given
        val string = buildString {
            for(i in 1..Constants.MAX_NAME_LENGTH+1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem(string, "5", "25.0")

        // When
        val valueStatus = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        // Then
        assertThat(valueStatus.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `given shopping item with too high amount, returns error`() {
        // Given
        viewModel.insertShoppingItem("name", "99999999999999", "25.0")

        // When
        val valueStatus = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        // Then
        assertThat(valueStatus.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

}