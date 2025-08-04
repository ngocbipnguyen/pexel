package com.bachnn.image

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bachnn.image.compose.Home
import com.bachnn.image.compose.Splash
import com.bachnn.image.compose.composescreen.HomeScreen
import com.bachnn.image.compose.composescreen.SplashScreen


@Composable
fun ImageApp() {
    val navController = rememberNavController()
    ImageNavHost(navController)
}

@Composable
fun ImageNavHost(navController: NavHostController) {
    NavHost(
        navController, startDestination = Splash,
    ) {
        composable<Splash> {
            SplashScreen(
                moveScreen = {
                    navController.navigate(route = Home)
                }
            )
        }

        composable<Home> {
            HomeScreen(onClickImage = {

            })
        }
    }
}

