package com.example.easymart.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.auth.LogoutUseCase
import com.example.easymart.domain.usecase.auth.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val cachedUser = getCurrentUserUseCase()
    private val _uiState = MutableStateFlow(
        ProfileUiState(
            isLoading = cachedUser == null,
            user = cachedUser
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProfileUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = it.user == null, error = null) }

            try {
                observeCurrentUserUseCase().collect { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = user
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun onOptionClick(tag: String) {
        viewModelScope.launch {
            when (tag) {
                "orders" -> _events.send(ProfileUiEvent.Navigate("orders"))
                "address" -> _events.send(ProfileUiEvent.Navigate("address"))
                "payment" -> _events.send(ProfileUiEvent.Navigate("payment"))
                "settings" -> _events.send(ProfileUiEvent.Navigate("settings"))
                "login" -> _events.send(ProfileUiEvent.Navigate("login"))
                "admin" -> _events.send(ProfileUiEvent.Navigate("admin"))
                "logout" -> handleLogout()
            }
        }
    }

    private suspend fun handleLogout() {
        runCatching {
            logoutUseCase()
        }.onSuccess {
            _events.send(ProfileUiEvent.LogoutSuccess)
        }.onFailure {
            _events.send(ProfileUiEvent.ShowMessage("Logout failed"))
        }
    }
}
