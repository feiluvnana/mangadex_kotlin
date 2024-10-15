package com.fln.mangadex.features.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fln.mangadex.common.LocalNavigation
import com.fln.mangadex.features.HistoryPage
import com.fln.mangadex.features.LibraryPage
import com.fln.mangadex.features.UpdatesPage
import com.fln.mangadex.features.more.MorePage

@Composable
fun HomePage() {
  val homeNavigator = LocalNavigation.current.home!!

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
          label = { Text("Library") })
        NavigationBarItem(
          selected = route == "updates",
          onClick = { homeNavigator.navigate("updates") },
          icon = { Icon(Icons.Rounded.Update, null) },
          label = { Text("Updates") })
        NavigationBarItem(
          selected = route == "history",
          onClick = { homeNavigator.navigate("history") },
          icon = { Icon(Icons.Rounded.History, null) },
          label = { Text("History") })
        NavigationBarItem(
          selected = route == "more",
          onClick = { homeNavigator.navigate("more") },
          icon = { Icon(Icons.Rounded.MoreHoriz, null) },
          label = { Text("More") })
      }
    }) {
      NavHost(
        navController = homeNavigator,
        startDestination = "library",
        modifier = Modifier.padding(it)) {
          composable("library") { LibraryPage() }
          composable("updates") { UpdatesPage() }
          composable("history") { HistoryPage() }
          composable("more") { MorePage() }
        }
    }
}
