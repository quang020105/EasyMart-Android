package com.example.easymart.presentation.ui.checkout

import org.junit.Test

class CheckoutViewModelTest {

    @Test
    fun initialState_isIdle() {
        // TODO: implement
    }

    @Test
    fun pay_withoutUser_emitsError() {
        // TODO: implement
    }

    @Test
    fun pay_withoutAddress_emitsError() {
        // TODO: implement
    }

    @Test
    fun pay_withoutPaymentMethod_emitsError() {
        // TODO: implement
    }

    @Test
    fun pay_withOnlineGateway_redirectsToProcessing() {
        // TODO: implement
    }

    @Test
    fun pay_withCod_navigatesToSuccess() {
        // TODO: implement
    }

    @Test
    fun pay_failure_emitsError() {
        // TODO: implement
    }

    @Test
    fun selectAddressClick_emitsNavigation() {
        // TODO: implement
    }

    @Test
    fun selectPaymentClick_emitsNavigation() {
        // TODO: implement
    }

    @Test
    fun startAutoProcessOrder_withOrder_startsWork() {
        // TODO: implement
    }

    @Test
    fun pollPayOsAndUpdate_updatesLocalStatus() {
        // TODO: implement
    }

    @Test
    fun markPayOsCancelled_updatesLocalStatus() {
        // TODO: implement
    }
}