package com.fln.mangadex.features.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fln.mangadex.R
import com.fln.mangadex.common.LocalNavigation
import kotlinx.coroutines.launch

@Preview
@Composable
fun MorePage(viewModel: MoreViewModel = hiltViewModel()) {
  val rootNavigator = LocalNavigation.current.root!!
  val moreState by viewModel.state.collectAsState()
  val coroutineScope = rememberCoroutineScope()

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
          modifier = Modifier.padding(top = 8.dp),
          leadingContent = { Icon(Icons.Rounded.CloudOff, null) },
          headlineContent = { Text("Downloaded only") },
          supportingContent = { Text("Filter all entries in your library") },
          trailingContent = {
            Switch(
              checked = moreState.downloadedOnly,
              onCheckedChange = {
                coroutineScope.launch { viewModel.toggleDownloadedOnly() }
              })
          })
      }
      item {
        ListItem(
          modifier = Modifier.padding(bottom = 8.dp),
          leadingContent = { Icon(Icons.Rounded.HistoryToggleOff, null) },
          headlineContent = { Text("Incognito mode") },
          supportingContent = { Text("Pauses reading history") },
          trailingContent = {
            Switch(
              checked = moreState.incognitoMode,
              onCheckedChange = {
                coroutineScope.launch { viewModel.toggleIncognitoMode() }
              })
          })
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
          modifier = Modifier.clickable { rootNavigator.navigate("settings") },
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
