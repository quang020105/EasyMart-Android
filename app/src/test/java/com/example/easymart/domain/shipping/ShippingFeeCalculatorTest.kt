package com.example.easymart.domain.shipping

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ShippingFeeCalculatorTest {

    @Test
    fun `calculate returns zero for an empty order`() {
        val result = ShippingFeePolicy.calculate(
            subtotalVnd = 0L,
            totalItemQuantity = 0
        )

        assertThat(result).isEqualTo(0L)
    }

    @Test
    fun `calculate returns standard fee for up to three items below free threshold`() {
        val result = ShippingFeePolicy.calculate(
            subtotalVnd = 499_000L,
            totalItemQuantity = 3
        )

        assertThat(result).isEqualTo(25_000L)
    }

    @Test
    fun `calculate returns large order fee for more than three items below free threshold`() {
        val result = ShippingFeePolicy.calculate(
            subtotalVnd = 499_000L,
            totalItemQuantity = 4
        )

        assertThat(result).isEqualTo(35_000L)
    }

    @Test
    fun `calculate returns zero at free shipping threshold`() {
        val result = ShippingFeePolicy.calculate(
            subtotalVnd = 500_000L,
            totalItemQuantity = 1
        )

        assertThat(result).isEqualTo(0L)
    }
}
