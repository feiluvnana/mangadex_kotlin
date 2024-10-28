package com.fln.mangadex

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.*
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.fln.mangadex.core.models.SecureScreenMode
import com.fln.mangadex.theme.AppTheme
import com.fln.mangadex.viewmodels.MoreViewModel
import com.fln.mangadex.views.home.HomePage
import com.fln.mangadex.views.settings.*
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CompositionLocalNaming")
val LocalValuesProvider =
  compositionLocalOf<LocalValues> { error("No navController provided") }

data class LocalValues(
  val rootNavigator: NavHostController,
  val homeNavigator: NavHostController
)

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
  @RequiresApi(Build.VERSION_CODES.P)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Thread.setDefaultUncaughtExceptionHandler { _, _ -> }

    setContent {
      val moreViewModel: MoreViewModel = hiltViewModel()
      val moreState by moreViewModel.state.collectAsStateWithLifecycle()
      val lifecycleOwner = LocalLifecycleOwner.current
      val biometricPrompt = BiometricPrompt(
        this as FragmentActivity,
        object : BiometricPrompt.AuthenticationCallback() {})
      val biometricPromptInfo =
        BiometricPrompt.PromptInfo.Builder().setTitle("Require unlock")
          .setSubtitle("Authenticate to confirm change").build()

      val downloadedOnlyDp by animateDpAsState(
        targetValue = if (moreState.downloadedOnly) 50.dp else 0.dp,
        animationSpec = tween(250),
        label = "downloadedOnlyDp"
      )
      val incognitoModeDp by animateDpAsState(
        targetValue = if (moreState.incognitoMode) (if (moreState.downloadedOnly) 30.dp else 50.dp) else 0.dp,
        animationSpec = tween(250),
        label = "incognitoModeDp"
      )

      DisposableEffect(listOf(lifecycleOwner, moreState)) {
        val observer = LifecycleEventObserver { source, event ->
          when (event) {
            Lifecycle.Event.ON_RESUME -> {
              biometricPrompt.authenticate(biometricPromptInfo)
            }

            else -> {}
          }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
      }

      LaunchedEffect(true) {
        moreViewModel.state.collect {
          enableEdgeToEdge(
            statusBarStyle = if (it.downloadedOnly || it.incognitoMode) SystemBarStyle.light(
              Color.TRANSPARENT,
              Color.TRANSPARENT
            ) else SystemBarStyle.dark(Color.TRANSPARENT)
          )
          when (it.secureScreen) {
            SecureScreenMode.Always ->
              window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

            SecureScreenMode.IncognitoMode ->
              if (it.incognitoMode)
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
              else
                window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

            else -> window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
          }
        }
      }

      AppTheme {
        CompositionLocalProvider(
          LocalValuesProvider provides LocalValues(
            rememberNavController(),
            rememberNavController()
          )
        ) {
          Column {
            Box(
              modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
                .height(downloadedOnlyDp)
                .padding(2.dp)
            ) {
              Text(
                "Downloaded Only",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.BottomCenter)
              )
            }
            Box(
              modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .height(incognitoModeDp)
                .padding(2.dp)
            ) {
              Text(
                "Incognito Mode",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.BottomCenter)
              )
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
              }
            }
          }
        }
      }
    }
  }
}
