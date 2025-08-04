package com.bachnn.image.compose.composescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachnn.image.R
import com.bachnn.image.viewmodels.SplashUiState
import com.bachnn.image.viewmodels.SplashViewModel

@Composable
fun SplashScreen(
    moveScreen: () -> Unit,
    viewmodel: SplashViewModel = hiltViewModel()
) {
    when (viewmodel.splashUiState) {
        is SplashUiState.Success -> {
            moveScreen()
        }
        else -> {
            Scaffold { padding ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(padding).fillMaxWidth().fillMaxHeight()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icons_message),
                        contentDescription = stringResource(id = R.string.splash_description),
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                    )
                }
            }
        }
    }
}