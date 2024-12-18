package com.fln.mangadex.views.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fln.mangadex.LocalValuesProvider
import com.fln.mangadex.core.models.LockWhenIdleMode
import com.fln.mangadex.core.models.SecureScreenMode
import com.fln.mangadex.utils.getActivity
import com.fln.mangadex.viewmodels.MoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityPage(moreViewModel: MoreViewModel = hiltViewModel()) {
  val rootNavigator = LocalValuesProvider.current.rootNavigator
  val moreState by moreViewModel.state.collectAsState()
  val coroutineScope = rememberCoroutineScope()
  val mainActivity = LocalContext.current.getActivity()

  Scaffold(topBar = {
    TopAppBar(navigationIcon = {
      Icon(Icons.AutoMirrored.Rounded.ArrowBack,
        null,
        modifier = Modifier
          .clickable { rootNavigator.popBackStack() }
          .padding(12.dp))
    }, title = { Text("Security") })
  }) { innerPadding ->
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
      item {
        ListItem(headlineContent = { Text("Require unlock", fontSize = 14.sp) },
          trailingContent = {
            Switch(checked = moreState.requireUnlock, onCheckedChange = {
              mainActivity.startBiometricActivity(onSuccess = {
                coroutineScope.launch { moreViewModel.toggleRequireUnlock() }
              })
            })
          })
      }
      if (moreState.requireUnlock) item {
        ListItem(headlineContent = {
          Text("Lock when idle", fontSize = 14.sp)
        },
          supportingContent = {
            Text(moreState.lockWhenIdle.name, fontSize = 12.sp)
          },
          modifier = Modifier.clickable { rootNavigator.navigate("lock_when_idle") })
      }
      item {
        ListItem(headlineContent = {
          Text("Hide notification content", fontSize = 14.sp)
        }, trailingContent = { Switch(checked = true, onCheckedChange = {}) })
      }
      item {
        ListItem(headlineContent = { Text("Secure screen", fontSize = 14.sp) },
          supportingContent = {
            Text(moreState.secureScreen.name, fontSize = 12.sp)
          },
          modifier = Modifier.clickable { rootNavigator.navigate("secure_screen") })
      }
      item {
        Icon(Icons.Outlined.Info,
          null,
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .size(20.dp))
      }
      item {
        Text("Secure screen hides app contents when switching apps and block screenshots",
          fontSize = 12.sp,
          lineHeight = 12.sp,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecureScreenModeSelectionDialog(moreViewModel: MoreViewModel = hiltViewModel()) {
  val rootNavigator = LocalValuesProvider.current.rootNavigator
  val moreState by moreViewModel.state.collectAsState()
  val coroutineScope = rememberCoroutineScope()

  BasicAlertDialog(onDismissRequest = { rootNavigator.popBackStack() },
    content = {
      Surface(shape = RoundedCornerShape(24.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Security",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp))
          for (ssm in SecureScreenMode.entries) {
            Row {
              RadioButton(selected = moreState.secureScreen == ssm, onClick = {
                coroutineScope.launch { moreViewModel.setSecureScreen(ssm) }
              }, modifier = Modifier.align(Alignment.CenterVertically))
              Text(ssm.name,
                modifier = Modifier.align(Alignment.CenterVertically))
            }
          }
          TextButton(modifier = Modifier
            .padding(top = 8.dp)
            .align(Alignment.End),
            onClick = { rootNavigator.popBackStack() }) { Text("Cancel") }
        }
      }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockWhenIdleModeSelectionDialog(moreViewModel: MoreViewModel = hiltViewModel()) {
  val rootNavigator = LocalValuesProvider.current.rootNavigator
  val moreState by moreViewModel.state.collectAsState()
  val coroutineScope = rememberCoroutineScope()
  val mainActivity = LocalContext.current.getActivity()

  BasicAlertDialog(onDismissRequest = { rootNavigator.popBackStack() },
    content = {
      Surface(shape = RoundedCornerShape(24.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Security",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp))
          for (lwim in LockWhenIdleMode.entries) {
            Row {
              RadioButton(selected = moreState.lockWhenIdle == lwim, onClick = {
                mainActivity.startBiometricActivity(onSuccess = {
                  coroutineScope.launch {
                    moreViewModel.setLockWhenIdle(lwim)
                    rootNavigator.popBackStack()
                  }
                })
              }, modifier = Modifier.align(Alignment.CenterVertically))
              Text(lwim.name,
                modifier = Modifier.align(Alignment.CenterVertically))
            }
          }
          TextButton(modifier = Modifier
            .padding(top = 8.dp)
            .align(Alignment.End),
            onClick = { rootNavigator.popBackStack() }) { Text("Cancel") }
        }
      }
    })
}