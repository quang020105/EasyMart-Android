package com.example.easymart.presentation.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.User
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.auth.ObserveCurrentUserUseCase
import com.example.easymart.domain.usecase.cart.AddToCartUseCase
import com.example.easymart.domain.usecase.cart.ClearAllCartsUseCase
import com.example.easymart.domain.usecase.cart.ObserveCartUseCase
import com.example.easymart.domain.usecase.cart.SyncCartUseCase
import com.example.easymart.domain.usecase.cart.UpdateCartQuantityUseCase
import com.example.easymart.presentation.ui.mock.mockSimpleProduct
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var observeCartUS: ObserveCartUseCase
    private lateinit var clearAllUS: ClearAllCartsUseCase
    private lateinit var addToCartUS: AddToCartUseCase
    private lateinit var updateQuantityUS: UpdateCartQuantityUseCase
    private lateinit var observeCurrentUserUS: ObserveCurrentUserUseCase
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var syncCartUseCase: SyncCartUseCase

    private lateinit var cartFlow: MutableStateFlow<List<CartItem>>
    private lateinit var viewModel: CartViewModel

    private val mockCartItem = CartItem(
        id = 1,
        product = mockSimpleProduct,
        quantity = 1,
        price = 100000.0
    )

    private val mockUser = User(
        id = "user-1",
        name = "Quang",
        email = "quang@example.com",
        phone = "0900000000",
        avatarUrl = null,
        createdAt = 1710000000000L
    )

    // danh sach cart item mẫu để test
    private val mockCartItems = listOf(
        CartItem(
            id = 1,
            product = mockSimpleProduct.copy(name = "Product 1"),
            quantity = 2,
            price = 100000.0,
        ),
        CartItem(
            id = 2,
            product = mockSimpleProduct.copy(name = "Product 2"),
            quantity = 1,
            price = 200000.0,
        ),
        CartItem(
            id = 3,
            product = mockSimpleProduct.copy(name = "Product 3"),
            quantity = 3,
            price = 150000.0
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        observeCartUS = mockk()
        clearAllUS = mockk(relaxed = true)
        addToCartUS = mockk()
        updateQuantityUS = mockk()
        observeCurrentUserUS = mockk()
        getCurrentUserUseCase = mockk()
        syncCartUseCase = mockk()

        cartFlow = MutableStateFlow(emptyList())

        every { observeCurrentUserUS.invoke() } returns flowOf(null)
        every { getCurrentUserUseCase.invoke() } returns null

        coEvery { observeCartUS.invoke(any()) } returns cartFlow
        coEvery { addToCartUS.invoke(any(), any()) } returns Unit
        coEvery { updateQuantityUS.invoke(any(), any()) } returns Unit
        coEvery { syncCartUseCase.invoke(any()) } returns Unit

        viewModel = CartViewModel(
            observeCartUS = observeCartUS,
            clearAllUS = clearAllUS,
            addToCartUS = addToCartUS,
            updateQuantityUS = updateQuantityUS,
            observeCurrentUserUS = observeCurrentUserUS,
            getCurrentUserUseCase = getCurrentUserUseCase,
            syncCartUseCase = syncCartUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun initialState_shouldHaveDefaultValues() = runTest {
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertThat(state.items).isEmpty()
        assertThat(state.checkedAll).isFalse()
        assertThat(state.pendingRemoveItem).isNull()
    }

    @Test
    fun observeCart_whenCartFlowEmits_itemsAreMappedToUiState() = runTest {
        cartFlow.value = mockCartItems
        advanceUntilIdle()

        val items = viewModel.uiState.value.items
        assertThat(items).hasSize(3)
        assertThat(items.first { it.id == 1 }.product.name).isEqualTo("Product 1")
        assertThat(items.first { it.id == 2 }.product.name).isEqualTo("Product 2")
        assertThat(items.first { it.id == 3 }.product.name).isEqualTo("Product 3")
    }


    @Test
    fun addProductToCart_whenSuccess_emitsSuccessMessage() = runTest {
        val eventDeferred = async { viewModel.event.first() }
        val product = mockSimpleProduct

        viewModel.addProductToCart(product)
        advanceUntilIdle()

        assertThat(eventDeferred.await()).isEqualTo(CartEvent.ShowMessage("Đã thêm vào giỏ hàng"))
        coVerify(exactly = 1) {
            addToCartUS.invoke(
                null,
                match {
                    it.id == 1 &&
                            it.product.name == "Product 1" &&
                            it.price == 100000.0
                }
            )
        }
    }

    @Test
    fun addProductToCart_whenFailed_emitsFailureMessage() = runTest {
        coEvery { addToCartUS.invoke(any(), any()) } throws RuntimeException("boom")
        val eventDeferred = async { viewModel.event.first() }
        val product = mockSimpleProduct

        viewModel.addProductToCart(product)
        advanceUntilIdle()

        assertThat(eventDeferred.await()).isEqualTo(CartEvent.ShowMessage("Không thể thêm sản phẩm"))
    }

    @Test
    fun addProductToCart_whenUserIsLoggedIn_passesUserIdToUseCase() = runTest {
        val user = mockUser
        every { getCurrentUserUseCase.invoke() } returns user

        val eventDeferred = async { viewModel.event.first() }
        val product = mockSimpleProduct

        viewModel.addProductToCart(product)
        advanceUntilIdle()

        assertThat(eventDeferred.await())
            .isEqualTo(CartEvent.ShowMessage("Đã thêm vào giỏ hàng"))

        coVerify(exactly = 1) {
            addToCartUS.invoke(
                user.id,
                match {
                    it.id == product.id &&
                            it.quantity == 1 &&
                            it.price == product.price &&
                            it.product.name == product.name

                }
            )
        }
    }

    @Test
    fun addProductToCart_whenUserIsLoggedIn_butAddToCartFails_emitsFailureMessage() = runTest {
        val user = mockUser
        every { getCurrentUserUseCase.invoke() } returns user
        coEvery { addToCartUS.invoke(any(), any()) } throws RuntimeException("boom")

        val eventDeferred = async { viewModel.event.first() }
        val product = mockSimpleProduct

        viewModel.addProductToCart(product)
        advanceUntilIdle()

        assertThat(eventDeferred.await())
            .isEqualTo(CartEvent.ShowMessage("Không thể thêm sản phẩm"))

        coVerify(exactly = 1) {
            addToCartUS.invoke(
                user.id,
                match {
                    it.id == product.id &&
                            it.quantity == 1 &&
                            it.price == product.price
                }
            )
        }
    }

    @Test
    fun updateQuantity_whenDecrementedToZero_setsPendingRemoveItem() = runTest {
        val item = mockCartItem

        viewModel.updateQuantity(item, delta = -1)
        advanceUntilIdle()
        assertThat(item).isEqualTo(viewModel.uiState.value.pendingRemoveItem)
    }

    @Test
    fun updateQuantity_whenDeltaIsValid_updatesQuantity() = runTest {
        val item = mockCartItem

        viewModel.updateQuantity(item, delta = 1)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            updateQuantityUS.invoke(item, 1)
        }
        assertThat(viewModel.uiState.value.pendingRemoveItem).isNull()
    }

    @Test
    fun updateQuantity_whenUseCaseFails_emitsFailureMessage() = runTest {
        val item = mockCartItem
        coEvery { updateQuantityUS.invoke(item, 1) } throws RuntimeException("boom")

        val eventDeferred = async { viewModel.event.first() }

        viewModel.updateQuantity(item, delta = 1)
        advanceUntilIdle()

        assertThat(eventDeferred.await())
            .isEqualTo(CartEvent.ShowMessage("Không thể cập nhật số lượng"))
    }

    @Test
    fun confirmRemovePendingItem_whenCalled_removesPendingItem() = runTest {
        val item = mockCartItem
        viewModel.updateQuantity(item, delta = -1)
        advanceUntilIdle()

        viewModel.confirmRemovePendingItem()
        advanceUntilIdle()

        coVerify(exactly = 1) {
            updateQuantityUS.invoke(item, -1)
        }
        //sau khi xác nhận xóa, pendingRemoveItem phải được reset về null
        assertThat(viewModel.uiState.value.pendingRemoveItem).isNull()
    }

    @Test
    fun confirmRemovePendingItem_whenNoPendingItem_doesNothing() = runTest {
        viewModel.confirmRemovePendingItem()
        advanceUntilIdle()

        coVerify(exactly = 0) {
            updateQuantityUS.invoke(any(), any())
        }
        assertThat(viewModel.uiState.value.pendingRemoveItem).isNull()
    }

    @Test
    fun confirmRemovePendingItem_whenUseCaseFails_emitsFailureMessage_andKeepsPendingItem() = runTest {
        val item = mockCartItem
        viewModel.updateQuantity(item, delta = -1)
        advanceUntilIdle()

        coEvery { updateQuantityUS.invoke(item, -1) } throws RuntimeException("boom")

        val eventDeferred = async { viewModel.event.first() }

        viewModel.confirmRemovePendingItem()
        advanceUntilIdle()

        assertThat(eventDeferred.await())
            .isEqualTo(CartEvent.ShowMessage("Không thể xóa sản phẩm"))
        assertThat(viewModel.uiState.value.pendingRemoveItem).isEqualTo(item)
    }



    @Test
    fun cancelRemovePendingItem_clearsPendingItem() = runTest {
        val item = mockCartItem
        viewModel.updateQuantity(item, delta = -1)
        advanceUntilIdle()

        viewModel.cancelRemovePendingItem()
        coVerify(exactly = 0) {
            updateQuantityUS.invoke(any(), any())
        }
        assertThat(viewModel.uiState.value.pendingRemoveItem).isNull()
    }

    @Test
    fun onCheckChanged_updatesItemCheckedState() = runTest {
        cartFlow.value = mockCartItems
        advanceUntilIdle()

        viewModel.onCheckChanged(id = 2, isChecked = true)

        val updatedItems = viewModel.uiState.value.items
        assertThat(updatedItems.first { it.id == 1 }.isChecked).isFalse()
        assertThat(updatedItems.first { it.id == 2 }.isChecked).isTrue()
    }

    @Test
    fun onCheckedChangeAll_updatesAllItemsChecked() = runTest {
        cartFlow.value = mockCartItems
        advanceUntilIdle()

        viewModel.onCheckedChangeAll(isChecked = true)

        val updatedItems = viewModel.uiState.value.items
        assertThat(updatedItems.all { it.isChecked }).isTrue()
        assertThat(viewModel.uiState.value.checkedAll).isTrue()
    }

    @Test
    fun onCheckedChangeAll_whenFalse_unchecksAllItems() = runTest {
        cartFlow.value = mockCartItems
        advanceUntilIdle()

        viewModel.onCheckedChangeAll(isChecked = true)
        viewModel.onCheckedChangeAll(isChecked = false)

        val updatedItems = viewModel.uiState.value.items
        assertThat(updatedItems.all { it.isChecked }).isFalse()
        assertThat(viewModel.uiState.value.checkedAll).isFalse()
    }

    @Test
    fun onCheckChanged_whenIdNotFound_keepsStateUnchanged() = runTest {
        cartFlow.value = mockCartItems
        advanceUntilIdle()

        val before = viewModel.uiState.value.items

        viewModel.onCheckChanged(id = 999, isChecked = true)

        assertThat(viewModel.uiState.value.items).isEqualTo(before)
    }

    @Test
    fun saveSelectedItemsForCheckout_savesPendingProductIds() = runTest {
        val item1 = mockCartItems[0].copy(
            product = mockCartItems[0].product.copy(id = 1),
        )
        val item2 = mockCartItems[1].copy(
            product = mockCartItems[1].product.copy(id = 2),
        )
        val item3 = mockCartItems[2].copy(
            product = mockCartItems[2].product.copy(id = 3),
        )
        cartFlow.value = listOf(item1, item2, item3)
        advanceUntilIdle()

        viewModel.onCheckChanged(id = 1, isChecked = true)
        viewModel.onCheckChanged(id = 2, isChecked = true)
        viewModel.saveSelectedItemsForCheckout()

        assertThat(
            getPrivateField<Set<Int>?>(viewModel, "pendingSelectedProductIds")
        ).isEqualTo(setOf(1, 2))
    }

    @Test
    fun saveSelectedItemsForCheckout_whenNothingChecked_doesNotSavePendingIds() = runTest {
        cartFlow.value = mockCartItems
        advanceUntilIdle()

        viewModel.saveSelectedItemsForCheckout()

        assertThat(getPrivateField<Set<Int>?>(viewModel, "pendingSelectedProductIds")).isNull()
    }

    @Test
    fun restoreSelectedItemsAfterLogin_whenNoPendingIds_doesNothing() = runTest {
        cartFlow.value = mockCartItems
        advanceUntilIdle()

        viewModel.restoreSelectedItemsAfterLogin()

        val updatedItems = viewModel.uiState.value.items
        assertThat(updatedItems.all { it.isChecked }).isFalse()
        assertThat(getPrivateField<Set<Int>?>(viewModel, "pendingSelectedProductIds")).isNull()
    }

    @Test
    fun restoreSelectedItemsAfterLogin_whenCurrentItemsEmpty_doesNothing() = runTest {
        setPrivateField(viewModel, "pendingSelectedProductIds", setOf(1, 2))

        viewModel.restoreSelectedItemsAfterLogin()

        assertThat(viewModel.uiState.value.items).isEmpty()
        assertThat(getPrivateField<Set<Int>?>(viewModel, "pendingSelectedProductIds"))
            .isEqualTo(setOf(1, 2))
    }

    @Test
    fun restoreSelectedItemsAfterLogin_whenPendingIdsMatch_restoresOnlyMatchedItems() = runTest {
        val item1 = mockCartItems[0].copy(
            product = mockCartItems[0].product.copy(id = 1),
        )
        val item2 = mockCartItems[1].copy(
            product = mockCartItems[1].product.copy(id = 2),
        )
        val item3 = mockCartItems[2].copy(
            product = mockCartItems[2].product.copy(id = 3),
        )
        cartFlow.value = listOf(item1, item2, item3)
        advanceUntilIdle()

        setPrivateField(viewModel, "pendingSelectedProductIds", setOf(1, 3))

        viewModel.restoreSelectedItemsAfterLogin()

        val updatedItems = viewModel.uiState.value.items
        assertThat(updatedItems.first { it.id == 1 }.isChecked).isTrue()
        assertThat(updatedItems.first { it.id == 2 }.isChecked).isFalse()
        assertThat(updatedItems.first { it.id == 3 }.isChecked).isTrue()
    }

    // lấy giá trị của trường private trong viewmodel
    @Suppress("UNCHECKED_CAST")
    private fun <T> getPrivateField(target: Any, fieldName: String): T {
        val field = target.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(target) as T
    }

    // thiết lập giá trị cho trường private trong viewmodel
    private fun setPrivateField(target: Any, fieldName: String, value: Any?) {
        val field = target.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(target, value)
    }
}