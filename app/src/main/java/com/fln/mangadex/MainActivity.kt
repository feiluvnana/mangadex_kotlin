package com.fln.mangadex

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.fln.mangadex.core.models.SecureScreenMode
import com.fln.mangadex.theme.AppTheme
import com.fln.mangadex.viewmodels.MoreViewModel
import com.fln.mangadex.views.home.HomePage
import com.fln.mangadex.views.settings.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

val LocalValuesProvider =
  compositionLocalOf<LocalValues> { error("No navController provided") }

data class LocalValues(val rootNavigator: NavHostController,
                       val homeNavigator: NavHostController)

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
  private lateinit var moreViewModel: MoreViewModel
  private val startActivityResultFlow: MutableStateFlow<ActivityResult?> =
    MutableStateFlow(null)
  private val startActivityForResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      lifecycleScope.launch {
        startActivityResultFlow.emit(it)
      }
    }

  fun startBiometricActivity(onCancelled: () -> Unit = {},
                             onSuccess: () -> Unit = {}) {
    startActivityForResultLauncher.launch(Intent(this,
      BiometricActivity::class.java).apply { //putExtra("isCancellable", isCancellable)
    })
    lifecycleScope.launch {
      startActivityResultFlow.drop(1).collect {
        Log.d("event", it.toString())
        if (it?.resultCode == RESULT_OK) {
          cancel()
          onSuccess()
        }
        if (it?.resultCode == RESULT_CANCELED) {
          cancel()
          onCancelled()
        }
      }
    }
  }

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  @RequiresApi(Build.VERSION_CODES.P)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      moreViewModel = hiltViewModel()
      val moreState by moreViewModel.state.collectAsStateWithLifecycle()
      val downloadedOnlyDp by animateDpAsState(targetValue = if (moreState.downloadedOnly) 50.dp else 0.dp,
        animationSpec = tween(250),
        label = "downloadedOnlyDp")
      val incognitoModeDp by animateDpAsState(targetValue = if (moreState.incognitoMode) (if (moreState.downloadedOnly) 30.dp else 50.dp) else 0.dp,
        animationSpec = tween(250),
        label = "incognitoModeDp")
      val rootNavigator = rememberNavController()
      val homeNavigator = rememberNavController()
      var isUnlockNecessary by rememberSaveable { mutableStateOf(true) }

      DisposableEffect(key1 = moreState.requireUnlock) {
        val observer = LifecycleEventObserver { _, event ->
          when {
            event == Lifecycle.Event.ON_START && isUnlockNecessary -> {
              if (moreState.requireUnlock) {
                startBiometricActivity(onSuccess = {
                  isUnlockNecessary = false
                })
              }
            }

            else -> {}
          }
        }
        lifecycle.addObserver(observer)
        onDispose {
          lifecycle.removeObserver(observer)
        }
      }

      LaunchedEffect(key1 = moreState.downloadedOnly,
        key2 = moreState.incognitoMode,
        key3 = moreState.secureScreen) {
        enableEdgeToEdge(statusBarStyle = if (moreState.downloadedOnly || moreState.incognitoMode) SystemBarStyle.light(
          Color.TRANSPARENT,
          Color.TRANSPARENT) else SystemBarStyle.dark(Color.TRANSPARENT))
        when (moreState.secureScreen) {
          SecureScreenMode.Always -> window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
          SecureScreenMode.IncognitoMode -> if (moreState.incognitoMode) window.addFlags(
            WindowManager.LayoutParams.FLAG_SECURE)
          else window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

          else -> window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
      }

      AppTheme {
        CompositionLocalProvider(LocalValuesProvider provides LocalValues(
          rootNavigator,
          homeNavigator)) {
          Column {
            Box(modifier = Modifier
              .background(MaterialTheme.colorScheme.secondary)
              .fillMaxWidth()
              .height(downloadedOnlyDp)
              .padding(2.dp)) {
              Text("Downloaded Only",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.BottomCenter))
            }
            Box(modifier = Modifier
              .background(MaterialTheme.colorScheme.primary)
              .fillMaxWidth()
              .height(incognitoModeDp)
              .padding(2.dp)) {
              Text("Incognito Mode",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.BottomCenter))
            }
            NavHost(navController = LocalValuesProvider.current.rootNavigator,
              startDestination = "home",
              enterTransition = { EnterTransition.None },
              exitTransition = { ExitTransition.None },
              popEnterTransition = { EnterTransition.None },
              popExitTransition = { ExitTransition.None }) {
              composable("home") { HomePage() }
              this.apply {
                composable("settings") { SettingsPage() }
                composable("security") { SecurityPage() }
                dialog("secure_screen") { SecureScreenModeSelectionDialog() }
                dialog("lock_when_idle") { LockWhenIdleModeSelectionDialog() }
              }
            }
          }
        }
      }
    }
  }
}
