package com.fln.mangadex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fln.mangadex.pages.LibraryPage
import com.fln.mangadex.pages.UpdatesPage

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val navController = rememberNavController()

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
          }
        }) {
          NavHost(navController, startDestination = "library") {
            composable("library") { LibraryPage() }
            composable("updates") { UpdatesPage() }
          }
        }
    }
  }
}
