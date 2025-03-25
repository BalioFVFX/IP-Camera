package com.ipcamera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ipcamera.ui.base.BackgroundColor
import com.ipcamera.ui.base.ComposeAppTheme
import com.ipcamera.ui.nav.NavigationRoute
import com.ipcamera.ui.nav.Navigator
import com.ipcamera.ui.screen.fps.FramesPerSecondScreen
import com.ipcamera.ui.screen.main.MainScreen
import com.ipcamera.ui.screen.settings.SettingScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackgroundColor)
                ) {
                    val bottomSheetNavigator = rememberBottomSheetNavigator()
                    val navController = rememberNavController(bottomSheetNavigator)
                    ModalBottomSheetLayout(
                        bottomSheetNavigator = bottomSheetNavigator,
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = NavigationRoute.Main,
                            builder = {
                                composable<NavigationRoute.Main> {
                                    MainScreen()
                                }

                                composable<NavigationRoute.Streaming> {

                                }

                                composable<NavigationRoute.Settings> {
                                    SettingScreen()
                                }

                                bottomSheet<NavigationRoute.FramesPerSecond> {
                                    FramesPerSecondScreen()
                                }
                            },
                        )
                    }

                    CollectOnce(navigator.navigationEvent) { route ->
                        navController.navigate(route)
                    }
                }
            }
        }
    }
}