package com.bachnn.image.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SplashUiState {
    object Success : SplashUiState
    object Loading : SplashUiState
}

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    var splashUiState: SplashUiState by mutableStateOf(SplashUiState.Loading)
        private set

    init {
        splashUiState = SplashUiState.Loading
        viewModelScope.launch {
            delay(3000)
            splashUiState = SplashUiState.Success
        }
    }

}
