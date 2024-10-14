package com.fln.mangadex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.fln.mangadex.pages.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Thread.setDefaultUncaughtExceptionHandler(
      object : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(p0: Thread, p1: Throwable) {}
      })

    setContent {
      val navController = rememberNavController()

      MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          bottomBar = {
            val route =
              navController
                .currentBackStackEntryAsState()
                .value
                ?.destination
                ?.route

            NavigationBar {
              NavigationBarItem(
                selected = route == "library",
                onClick = { navController.navigate("library") },
                icon = { Icon(Icons.Rounded.Book, null) },
                label = { Text("Library") })
              NavigationBarItem(
                selected = route == "updates",
                onClick = { navController.navigate("updates") },
                icon = { Icon(Icons.Rounded.Update, null) },
                label = { Text("Updates") })
              NavigationBarItem(
                selected = route == "history",
                onClick = { navController.navigate("history") },
                icon = { Icon(Icons.Rounded.History, null) },
                label = { Text("History") })
              NavigationBarItem(
                selected = route == "more",
                onClick = { navController.navigate("more") },
                icon = { Icon(Icons.Rounded.MoreHoriz, null) },
                label = { Text("More") })
            }
          }) {
            NavHost(
              navController = navController,
              startDestination = "library",
              modifier = Modifier.padding(it)) {
                composable("library") { LibraryPage() }
                composable("updates") { UpdatesPage() }
                composable("history") { HistoryPage() }
                composable("more") { MorePage() }
              }
          }
      }
    }
  }
}
