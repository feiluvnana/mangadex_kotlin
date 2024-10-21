package com.fln.mangadex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fln.mangadex.common.FlnNavHostControllers
import com.fln.mangadex.common.LocalNavigation
import com.fln.mangadex.features.home.HomePage
import com.fln.mangadex.features.settings.SettingsPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(darkColorScheme().surface.toArgb()))
    Thread.setDefaultUncaughtExceptionHandler { _, _ -> }

    setContent {
      MaterialTheme(colorScheme = darkColorScheme()) {
        CompositionLocalProvider(
          LocalNavigation provides
            FlnNavHostControllers(
              rememberNavController(), rememberNavController())) {
            NavHost(
              navController = LocalNavigation.current.root!!,
              startDestination = "home") {
                composable("home") { HomePage() }
                composable("settings") { SettingsPage() }
              }
          }
      }
    }
  }
}
