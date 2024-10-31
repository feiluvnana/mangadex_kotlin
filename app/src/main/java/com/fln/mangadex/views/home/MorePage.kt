package com.fln.mangadex.views.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Label
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.fln.mangadex.LocalValuesProvider
import com.fln.mangadex.R
import com.fln.mangadex.viewmodels.MoreViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MorePage(moreViewModel: MoreViewModel = hiltViewModel()) {
  val rootNavigator = LocalValuesProvider.current.rootNavigator
  val moreState by moreViewModel.state.collectAsState()
  val coroutineScope = rememberCoroutineScope()

  Scaffold { _ ->
    val screenSize: Pair<Dp, Dp> = Pair(
      LocalConfiguration.current.screenWidthDp.dp,
      LocalConfiguration.current.screenHeightDp.dp,
    )

    LazyColumn {
      item {
        Box(modifier = Modifier
          .fillMaxWidth()
          .height(screenSize.second / 4)) {
          Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center),
          )
        }
      }
      item { HorizontalDivider() }
      item {
        ListItem(modifier = Modifier.padding(top = 8.dp), leadingContent = {
          Icon(Icons.Rounded.CloudOff,
            null,
            tint = MaterialTheme.colorScheme.primary)
        }, headlineContent = {
          Text("Downloaded only",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold)
        }, supportingContent = {
          Text("Filter all entries in your library", fontSize = 12.sp)
        }, trailingContent = {
          Switch(checked = moreState.downloadedOnly, onCheckedChange = {
            coroutineScope.launch { moreViewModel.toggleDownloadedOnly() }
          })
        })
      }
      item {
        ListItem(modifier = Modifier.padding(bottom = 8.dp), leadingContent = {
          Icon(Icons.Rounded.HistoryToggleOff,
            null,
            tint = MaterialTheme.colorScheme.primary)
        }, headlineContent = {
          Text("Incognito mode",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold)
        }, supportingContent = {
          Text("Pauses reading history", fontSize = 12.sp)
        }, trailingContent = {
          Switch(checked = moreState.incognitoMode, onCheckedChange = {
            coroutineScope.launch { moreViewModel.toggleIncognitoMode() }
          })
        })
      }
      item { HorizontalDivider() }
      item {
        ListItem(leadingContent = {
          Icon(Icons.Rounded.Download,
            null,
            tint = MaterialTheme.colorScheme.primary)
        }, headlineContent = {
          Text("Download queue",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold)
        })
      }
      item {
        ListItem(leadingContent = {
          Icon(Icons.AutoMirrored.Rounded.Label,
            null,
            tint = MaterialTheme.colorScheme.primary)
        }, headlineContent = {
          Text("Categories", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        })
      }
      item {
        ListItem(leadingContent = {
          Icon(Icons.Rounded.QueryStats,
            null,
            tint = MaterialTheme.colorScheme.primary)
        }, headlineContent = {
          Text("Statistics", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        })
      }
      item {
        ListItem(leadingContent = {
          Icon(Icons.Rounded.SettingsBackupRestore,
            null,
            tint = MaterialTheme.colorScheme.primary)
        }, headlineContent = {
          Text("Backup and restore",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold)
        })
      }
      item { HorizontalDivider() }
      item {
        ListItem(modifier = Modifier.clickable { rootNavigator.navigate("settings") },
          leadingContent = {
            Icon(Icons.Rounded.Settings,
              null,
              tint = MaterialTheme.colorScheme.primary)
          },
          headlineContent = {
            Text("Settings", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
          })
      }
      item {
        ListItem(leadingContent = {
          Icon(Icons.Rounded.Info,
            null,
            tint = MaterialTheme.colorScheme.primary)
        }, headlineContent = {
          Text("About", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        })
      }
      item {
        ListItem(leadingContent = {
          Icon(Icons.AutoMirrored.Rounded.Help,
            null,
            tint = MaterialTheme.colorScheme.primary)
        }, headlineContent = {
          Text("Help", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        })
      }
    }
  }
}



