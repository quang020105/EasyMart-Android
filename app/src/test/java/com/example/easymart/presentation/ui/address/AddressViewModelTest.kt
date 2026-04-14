package com.example.easymart.presentation.ui.address

import org.junit.Test
import com.example.easymart.domain.model.Ward
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.easymart.domain.model.Address
import com.example.easymart.domain.model.District
import com.example.easymart.domain.model.Province
import com.example.easymart.domain.usecase.address.DeleteAddressUseCase
import com.example.easymart.domain.usecase.address.GetAddressByIdUseCase
import com.example.easymart.domain.usecase.address.GetAllAddressUseCase
import com.example.easymart.domain.usecase.address.GetDefaultAddressUseCase
import com.example.easymart.domain.usecase.address.InsertNewAddressUseCase
import com.example.easymart.domain.usecase.address.SetDefaultAddressUseCase
import com.example.easymart.domain.usecase.address.UpdateAddressUseCase
import com.example.easymart.domain.usecase.location.GetDistrictsUseCase
import com.example.easymart.domain.usecase.location.GetProvincesUseCase
import com.example.easymart.domain.usecase.location.GetWardsUseCase
import com.example.easymart.presentation.ui.deliveryaddress.AddressViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.invoke

@OptIn(ExperimentalCoroutinesApi::class)
class AddressViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var getAllAddressUS: GetAllAddressUseCase
    private lateinit var insertAddressUS: InsertNewAddressUseCase
    private lateinit var updateAddressUS: UpdateAddressUseCase
    private lateinit var deleteAddressUS: DeleteAddressUseCase
    private lateinit var getAddressByIdUS: GetAddressByIdUseCase
    private lateinit var setDefaultAddressUS: SetDefaultAddressUseCase
    private lateinit var getDefaultAddressUseCase: GetDefaultAddressUseCase
    private lateinit var getProvincesUS: GetProvincesUseCase
    private lateinit var getDistrictsUS: GetDistrictsUseCase
    private lateinit var getWardsUS: GetWardsUseCase

    private lateinit var viewModel: AddressViewModel

    private val province = mockProvince(code = 1, name = "Hà Nội")
    private val district = mockDistrict(code = 10, name = "Ba Đình")
    private val ward = mockWard(code = 100, name = "Phúc Xá")

    private val defaultAddress = Address(
        id = 7,
        name = "Nguyễn Văn A",
        phone = "0912345678",
        detailAddress = "Số 1 Trần Phú",
        addressString = "Số 1 Trần Phú, Phúc Xá, Ba Đình, Hà Nội",
        isDefault = true,
        provinceCode = 1,
        districtCode = 10,
        wardCode = 100
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getAllAddressUS = mockk()
        insertAddressUS = mockk()
        updateAddressUS = mockk()
        deleteAddressUS = mockk()
        getAddressByIdUS = mockk()
        setDefaultAddressUS = mockk(relaxed = true)
        getDefaultAddressUseCase = mockk()
        getProvincesUS = mockk()
        getDistrictsUS = mockk()
        getWardsUS = mockk()

        every { getAllAddressUS.invoke() } returns flowOf(emptyList())
        coEvery { getDefaultAddressUseCase.invoke() } returns defaultAddress

        coEvery { getProvincesUS.invoke() } returns listOf(province)
        coEvery { getDistrictsUS.invoke(any()) } returns listOf(district)
        coEvery { getWardsUS.invoke(any()) } returns listOf(ward)

        coEvery { insertAddressUS.invoke(any()) } returns 99
        coEvery { updateAddressUS.invoke(any()) } returns Unit
        coEvery { deleteAddressUS.invoke(any()) } returns Unit
        coEvery { getAddressByIdUS.invoke(any()) } returns null

        viewModel = AddressViewModel(
            getALlAddressUS = getAllAddressUS,
            insertAddressUS = insertAddressUS,
            updateAddressUS = updateAddressUS,
            deleteAddressUS = deleteAddressUS,
            getAddressByIdUS = getAddressByIdUS,
            setDefaultAddressUS = setDefaultAddressUS,
            getDefaultAddressUseCase = getDefaultAddressUseCase,
            getProvincesUS = getProvincesUS,
            getDistrictsUS = getDistrictsUS,
            getWardsUS = getWardsUS
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_shouldLoadDefaultAddress() = runTest {
        advanceUntilIdle()

        assertThat(viewModel.selectedAddress.value).isEqualTo(defaultAddress)
    }

    @Test
    fun loadDefaultAddress_whenUseCaseReturnsNull_setsSelectedAddressToNull() = runTest {
        val viewModel = createViewModelWithDefaultAddress(null)
        advanceUntilIdle()

        assertThat(viewModel.selectedAddress.value).isNull()
    }










    private fun mockProvince(code: Int, name: String): Province {
        return mockk<Province>().apply {
            every { this@apply.code } returns code
            every { this@apply.name } returns name
        }
    }

    private fun mockDistrict(code: Int, name: String): District {
        return mockk<District>().apply {
            every { this@apply.code } returns code
            every { this@apply.name } returns name
        }
    }

    private fun mockWard(code: Int, name: String): Ward {
        return mockk<Ward>().apply {
            every { this@apply.code } returns code
            every { this@apply.name } returns name
        }
    }


    // khởi tạo ViewModel với giá trị default address khác nhau để test
    private fun createViewModelWithDefaultAddress(defaultAddressValue: Address?): AddressViewModel {
        val allAddressUS = mockk<GetAllAddressUseCase>()
        val insertUS = mockk<InsertNewAddressUseCase>()
        val updateUS = mockk<UpdateAddressUseCase>()
        val deleteUS = mockk<DeleteAddressUseCase>()
        val getByIdUS = mockk<GetAddressByIdUseCase>()
        val setDefaultUS = mockk<SetDefaultAddressUseCase>(relaxed = true)
        val defaultUS = mockk<GetDefaultAddressUseCase>()
        val provincesUS = mockk<GetProvincesUseCase>()
        val districtsUS = mockk<GetDistrictsUseCase>()
        val wardsUS = mockk<GetWardsUseCase>()

        every { allAddressUS.invoke() } returns flowOf(emptyList())
        coEvery { defaultUS.invoke() } returns defaultAddressValue
        coEvery { provincesUS.invoke() } returns emptyList()
        coEvery { districtsUS.invoke(any()) } returns emptyList()
        coEvery { wardsUS.invoke(any()) } returns emptyList()
        coEvery { insertUS.invoke(any()) } returns 1
        coEvery { updateUS.invoke(any()) } returns Unit
        coEvery { deleteUS.invoke(any()) } returns Unit
        coEvery { getByIdUS.invoke(any()) } returns null

        return AddressViewModel(
            getALlAddressUS = allAddressUS,
            insertAddressUS = insertUS,
            updateAddressUS = updateUS,
            deleteAddressUS = deleteUS,
            getAddressByIdUS = getByIdUS,
            setDefaultAddressUS = setDefaultUS,
            getDefaultAddressUseCase = defaultUS,
            getProvincesUS = provincesUS,
            getDistrictsUS = districtsUS,
            getWardsUS = wardsUS
        )
    }

}