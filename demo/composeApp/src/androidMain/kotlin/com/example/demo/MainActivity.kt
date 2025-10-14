package com.example.demo

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.example.project.conference.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        setContent {
            ConferenceAppTheme {
                ConferenceApp()
            }
        }
    }
}

private val LightColors = lightColorScheme()

@Composable
fun ConferenceAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}

@Composable
fun ConferenceApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "conference_list"
    ) {
        composable("conference_list") {
            ConferenceListScreen(
                onConferenceClick = { conference ->
                    navController.navigate("conference_detail/${conference.id}")
                }
            )
        }
        composable(
            "conference_detail/{conferenceId}",
            arguments = listOf(navArgument("conferenceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val conferenceId = backStackEntry.arguments?.getString("conferenceId") ?: ""
            ConferenceDetailScreen(
                conferenceId = conferenceId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}