package com.bachnn.image

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bachnn.image.compose.Collection
import com.bachnn.image.compose.Full
import com.bachnn.image.compose.Home
import com.bachnn.image.compose.PhotoCollection
import com.bachnn.image.compose.Splash
import com.bachnn.image.compose.composescreen.CollectionScreen
import com.bachnn.image.compose.composescreen.FullScreen
import com.bachnn.image.compose.composescreen.HomeScreen
import com.bachnn.image.compose.composescreen.PhotoCollectionScreen
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
                    navController.navigate(route = Collection)
                }
            )
        }

        composable<Collection> {
            CollectionScreen(onClickCollection =  { it ->
                navController.navigate(route = PhotoCollection(
                    id = it.id,
                    title = it.title
                ))
            })
        }

        composable<PhotoCollection> { backStackEntry ->
            val photoCollection: PhotoCollection = backStackEntry.toRoute()
            PhotoCollectionScreen(
                id = photoCollection.id,
                title = photoCollection.title,
                mediaOnClick = {

                }
            )
        }

        composable<Home> {
            HomeScreen(onClickImage = { pexelsPhoto ->
                navController.navigate(route = Full(
                    imageId = pexelsPhoto.id.toString(),
                    originalUrl = pexelsPhoto.src.original,
                    mediumUrl = pexelsPhoto.src.medium,
                    smallUrl = pexelsPhoto.src.small,
                    photographer = pexelsPhoto.photographer
                ))
            })
        }

        composable<Full> { backStackEntry ->
            val full: Full = backStackEntry.toRoute()
            FullScreen(
                imageId = full.imageId,
                originalUrl = full.originalUrl,
                mediumUrl = full.mediumUrl,
                smallUrl = full.smallUrl,
                photographer = full.photographer
            )
        }
    }
}

