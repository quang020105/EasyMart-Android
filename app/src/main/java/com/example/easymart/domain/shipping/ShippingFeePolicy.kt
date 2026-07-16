package com.example.easymart.domain.shipping

object ShippingFeePolicy {
    private const val FREE_SHIPPING_THRESHOLD_VND = 500_000L
    private const val STANDARD_ITEM_LIMIT = 3
    private const val STANDARD_SHIPPING_FEE_VND = 25_000L
    private const val LARGE_ORDER_SHIPPING_FEE_VND = 35_000L

    fun calculate(subtotalVnd: Long, totalItemQuantity: Int): Long {
        require(subtotalVnd >= 0L) { "Subtotal must not be negative" }
        require(totalItemQuantity >= 0) { "Total item quantity must not be negative" }

        if (totalItemQuantity == 0 || subtotalVnd >= FREE_SHIPPING_THRESHOLD_VND) {
            return 0L
        }

        return if (totalItemQuantity <= STANDARD_ITEM_LIMIT) {
            STANDARD_SHIPPING_FEE_VND
        } else {
            LARGE_ORDER_SHIPPING_FEE_VND
        }
    }
}
