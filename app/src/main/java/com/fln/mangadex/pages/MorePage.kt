package com.fln.mangadex.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.HistoryToggleOff
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import com.fln.mangadex.R

@Composable
fun MorePage() {
  Scaffold {
    MaterialTheme.colorScheme
    val screenSize: Pair<Dp, Dp> =
      Pair(
        LocalConfiguration.current.screenWidthDp.dp,
        LocalConfiguration.current.screenHeightDp.dp)
    LocalConfiguration.current.densityDpi

    LazyColumn {
      item {
        Box(modifier = Modifier.fillMaxWidth().height(screenSize.second / 4)) {
          Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center))
        }
      }
      item { HorizontalDivider() }
      item {
        ListItem(
          leadingContent = { Icon(Icons.Rounded.CloudOff, null) },
          headlineContent = { Text("Downloaded only") },
          supportingContent = { Text("Filter all entries in your library") },
          trailingContent = { Switch(checked = false, onCheckedChange = {}) })
      }
      item {
        ListItem(
          leadingContent = { Icon(Icons.Rounded.HistoryToggleOff, null) },
          headlineContent = { Text("Incognito mode") },
          supportingContent = { Text("Pauses reading history") },
          trailingContent = { Switch(checked = false, onCheckedChange = {}) })
      }
      item { HorizontalDivider() }
      item {
        ListItem(
          leadingContent = { Icon(Icons.Rounded.Download, null) },
          headlineContent = { Text("Download queue") })
      }
      item {
        ListItem(
          leadingContent = { Icon(Icons.AutoMirrored.Rounded.Label, null) },
          headlineContent = { Text("Categories") })
      }
      item {
        ListItem(
          leadingContent = { Icon(Icons.Rounded.QueryStats, null) },
          headlineContent = { Text("Statistics") })
      }
      item {
        ListItem(
          leadingContent = { Icon(Icons.Rounded.SettingsBackupRestore, null) },
          headlineContent = { Text("Backup and restore") })
      }
      item { HorizontalDivider() }
      item {
        ListItem(
          leadingContent = { Icon(Icons.Rounded.Settings, null) },
          headlineContent = { Text("Settings") })
      }
      item {
        ListItem(
          leadingContent = { Icon(Icons.Rounded.Info, null) },
          headlineContent = { Text("About") })
      }
      item {
        ListItem(
          leadingContent = { Icon(Icons.AutoMirrored.Rounded.Help, null) },
          headlineContent = { Text("Help") })
      }
    }
  }
}
