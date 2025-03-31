package com.ipcamera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.ipcamera.ui.base.BackgroundColor
import com.ipcamera.ui.base.BottomSheetCornerRadius
import com.ipcamera.ui.base.ComposeAppTheme
import com.ipcamera.ui.nav.NavigationEvent
import com.ipcamera.ui.nav.NavigationRoute
import com.ipcamera.ui.nav.Navigator
import com.ipcamera.ui.screen.main.MainScreen
import com.ipcamera.ui.screen.settings.SettingScreen
import com.ipcamera.ui.screen.settings.fps.FramesPerSecondScreen
import com.ipcamera.ui.screen.settings.ip.ServerIpAddressScreen
import com.ipcamera.ui.screen.settings.resolution.ResolutionScreen
import com.ipcamera.ui.screen.settings.server.VideoServerScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

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
                        sheetShape = RoundedCornerShape(
                            topStart = BottomSheetCornerRadius,
                            topEnd = BottomSheetCornerRadius,
                        ),

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

                                dialog<NavigationRoute.Resolution> {
                                    ResolutionScreen()
                                }

                                bottomSheet<NavigationRoute.ServerIpAddress> {
                                    ServerIpAddressScreen()
                                }

                                bottomSheet<NavigationRoute.FramesPerSecond> {
                                    FramesPerSecondScreen()
                                }

                                dialog<NavigationRoute.VideoServer> {
                                    VideoServerScreen()
                                }
                            },
                        )
                    }

                    CollectOnce(navigator.navigationEvent) { event ->
                        when (event) {
                            NavigationEvent.Dismiss -> navController.popBackStack()
                            is NavigationEvent.NavigateToRoute -> navController.navigate(event.route)
                        }

                    }
                }
            }
        }
    }
}