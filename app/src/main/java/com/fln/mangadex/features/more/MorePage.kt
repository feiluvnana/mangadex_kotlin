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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fln.mangadex.GlobalValuesProvider
import com.fln.mangadex.R
import kotlinx.coroutines.launch

@Preview
@Composable
fun MorePage(viewModel: MoreViewModel = hiltViewModel()) {
  val rootNavigator = GlobalValuesProvider.current.rootNavigator!!
  val moreState by viewModel.state.collectAsState()
  val coroutineScope = rememberCoroutineScope()

  Scaffold { innerPadding ->
    val screenSize: Pair<Dp, Dp> =
      Pair(
        LocalConfiguration.current.screenWidthDp.dp,
        LocalConfiguration.current.screenHeightDp.dp,
      )

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(screenSize.second / 4)
        ) {
          Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center),
          )
        }
      }
      item { HorizontalDivider() }
      item {
        ListItem(
          modifier = Modifier.padding(top = 8.dp),
          leadingContent = {
            Icon(
              Icons.Rounded.CloudOff,
              null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Downloaded only",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "Filter all entries in your library",
              fontSize = 12.sp
            )
          },
          trailingContent = {
            Switch(
              checked = moreState.downloadedOnly,
              onCheckedChange = {
                coroutineScope.launch { viewModel.toggleDownloadedOnly() }
              },
            )
          },
        )
      }
      item {
        ListItem(
          modifier = Modifier.padding(bottom = 8.dp),
          leadingContent = {
            Icon(
              Icons.Rounded.HistoryToggleOff, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Incognito mode",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "Pauses reading history",
              fontSize = 12.sp
            )
          },
          trailingContent = {
            Switch(
              checked = moreState.incognitoMode,
              onCheckedChange = {
                coroutineScope.launch { viewModel.toggleIncognitoMode() }
              },
            )
          },
        )
      }
      item { HorizontalDivider() }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.Download, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Download queue",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.AutoMirrored.Rounded.Label, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Categories",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.QueryStats, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Statistics",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.SettingsBackupRestore, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Backup and restore",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
        )
      }
      item { HorizontalDivider() }
      item {
        ListItem(
          modifier = Modifier.clickable { rootNavigator.navigate("settings") },
          leadingContent = {
            Icon(
              Icons.Rounded.Settings, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Settings",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.Info, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "About",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.AutoMirrored.Rounded.Help, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Help",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
        )
      }
    }
  }
}
