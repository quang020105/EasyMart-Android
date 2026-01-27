package com.example.easymart.presentation.common

import com.example.easymart.presentation.common.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

// quản lý sự kiện toàn cục của ứng dụng
object AppEventBus {
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    suspend fun send(event: UiEvent) {
        _events.send(event)
    }
}