package com.fln.mangadex

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.*
import androidx.activity.compose.setContent
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.fln.mangadex.viewmodels.MoreViewModel
import com.fln.mangadex.views.home.HomePage
import com.fln.mangadex.views.settings.*
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CompositionLocalNaming")
val LocalValuesProvider = compositionLocalOf { LocalValues() }

data class LocalValues(
  val rootNavigator: NavHostController? = null,
  val homeNavigator: NavHostController? = null,
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Thread.setDefaultUncaughtExceptionHandler { _, _ -> }

    setContent {
      val moreViewModel: MoreViewModel = hiltViewModel()
      val moreState by moreViewModel.state.collectAsStateWithLifecycle()
      val downloadedOnlyDp by animateDpAsState(targetValue = if (moreState.downloadedOnly) 50.dp else 0.dp,
        animationSpec = tween(250),
        label = "downloadedOnlyDp")
      val incognitoModeDp by animateDpAsState(targetValue = if (moreState.incognitoMode) (if (moreState.downloadedOnly) 30.dp else 50.dp) else 0.dp,
        animationSpec = tween(250),
        label = "incognitoModeDp")

      LaunchedEffect(moreState.downloadedOnly || moreState.incognitoMode) {
        moreViewModel.state.collect {
          enableEdgeToEdge(statusBarStyle = if (moreState.downloadedOnly || moreState.incognitoMode) SystemBarStyle.light(
            Color.TRANSPARENT,
            Color.TRANSPARENT) else SystemBarStyle.dark(Color.TRANSPARENT))
        }
      }

      MaterialTheme(colorScheme = darkColorScheme(), shapes = Shapes()) {
        CompositionLocalProvider(LocalValuesProvider provides LocalValues(
          rememberNavController(),
          rememberNavController())) {
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
            NavHost(navController = LocalValuesProvider.current.rootNavigator!!,
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
