package com.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.repository.AuthRepository
import com.presentation.auth.AuthState
import com.presentation.common.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // login function
    fun login(username: String, password: String) {
        viewModelScope.launch {
            authRepository.login(username, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            user = result.data,
                            isLoggedIn = true,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _authState.value = _authState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                    is Resource.Loading -> {
                        _authState.value = _authState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout().collect { result ->
                if (result is Resource.Success) {
                    _authState.value = AuthState()
                    onSuccess()
                }
            }
        }
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}