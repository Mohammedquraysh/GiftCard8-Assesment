package com.example.giftcard8testapp

import androidx.room.compiler.processing.util.Resource
import com.data.repository.AuthRepository
import com.domain.model.User
import com.presentation.auth.viewmodel.AuthViewModel
import com.presentation.common.resource.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = AuthViewModel(authRepository)
    }

    @Test
    fun `login success should update state correctly`(): Unit = runTest {
        // Given
        val user = User("1", "test@example.com", "testuser")
        whenever(authRepository.login("testuser", "password"))
            .thenReturn(flowOf(Resource.Success(user)))

        // When
        viewModel.login("testuser", "password")

        // Then
        val state = viewModel.authState.value
        assertTrue(state.isLoggedIn)
        assertTrue(state.user?.username == "testuser")
    }
}