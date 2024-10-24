package com.fln.mangadex.views.home

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.fln.mangadex.LocalValuesProvider

@Composable
fun HomePage() {
  val homeNavigator = LocalValuesProvider.current.homeNavigator!!

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      val route =
        homeNavigator.currentBackStackEntryAsState().value?.destination?.route

      NavigationBar {
        NavigationBarItem(
          selected = route == "library",
          onClick = { homeNavigator.navigate("library") },
          icon = { Icon(Icons.Rounded.Book, null) },
          label = { Text("Library") },
        )
        NavigationBarItem(
          selected = route == "updates",
          onClick = { homeNavigator.navigate("updates") },
          icon = { Icon(Icons.Rounded.Update, null) },
          label = { Text("Updates") },
        )
        NavigationBarItem(
          selected = route == "history",
          onClick = { homeNavigator.navigate("history") },
          icon = { Icon(Icons.Rounded.History, null) },
          label = { Text("History") },
        )
        NavigationBarItem(
          selected = route == "more",
          onClick = { homeNavigator.navigate("more") },
          icon = { Icon(Icons.Rounded.MoreHoriz, null) },
          label = { Text("More") },
        )
      }
    },
  ) {
    NavHost(navController = homeNavigator,
      startDestination = "library",
      modifier = Modifier.padding(it),
      enterTransition = { EnterTransition.None },
      exitTransition = { ExitTransition.None },
      popEnterTransition = { EnterTransition.None },
      popExitTransition = { ExitTransition.None }) {
      composable("library") { LibraryPage() }
      composable("updates") { UpdatesPage() }
      composable("history") { HistoryPage() }
      composable("more") { MorePage() }
    }
  }
}
