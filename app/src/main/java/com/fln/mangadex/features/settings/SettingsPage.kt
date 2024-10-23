package com.fln.mangadex.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ChromeReaderMode
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.CollectionsBookmark
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fln.mangadex.GlobalValuesProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
  val rootNavigator = GlobalValuesProvider.current.rootNavigator!!

  Scaffold(topBar = {
    TopAppBar(navigationIcon = {
      Icon(
        Icons.AutoMirrored.Rounded.ArrowBack,
        null,
        modifier = Modifier
          .clickable {
            rootNavigator.popBackStack()
          }
          .padding(12.dp)
      )
    }, title = { Text("Settings") })
  }) { innerPadding ->
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.Tune, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "General",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "App language, notifications",
              fontSize = 12.sp
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.Palette, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Appearance",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "Theme, date & time format",
              fontSize = 12.sp
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.CollectionsBookmark, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Library",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "Categories, global update",
              fontSize = 12.sp
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.AutoMirrored.Rounded.ChromeReaderMode, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Reader",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "Reading mode, display, navigation",
              fontSize = 12.sp
            )
          },
        )
      }
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
              "Download",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "Automatic download, download ahead",
              fontSize = 12.sp
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.Sync, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Tracking",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "One-way progress sync, enhanced sync",
              fontSize = 12.sp
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.Explore, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Browse",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "Sources, extensions, global search",
              fontSize = 12.sp
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
          supportingContent = {
            Text(
              "Manual & automatic backups",
              fontSize = 12.sp
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.Security, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Security",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "App lock, secure screen",
              fontSize = 12.sp
            )
          },
        )
      }
      item {
        ListItem(
          leadingContent = {
            Icon(
              Icons.Rounded.Code, null,
              tint = MaterialTheme.colorScheme.primary
            )
          },
          headlineContent = {
            Text(
              "Advanced",
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          supportingContent = {
            Text(
              "Dump crash logs, battery optimization",
              fontSize = 12.sp
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
          supportingContent = {
            Text(
              "Tachiyomi Stable 0.14.3",
              fontSize = 12.sp
            )
          },
        )
      }
    }
  }
}
