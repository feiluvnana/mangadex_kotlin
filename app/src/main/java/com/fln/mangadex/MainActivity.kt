package com.fln.mangadex

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fln.mangadex.features.home.HomePage
import com.fln.mangadex.features.more.MoreViewModel
import com.fln.mangadex.features.settings.SettingsPage
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CompositionLocalNaming")
val GlobalValuesProvider = compositionLocalOf { GlobalValues() }

data class GlobalValues(
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

      LaunchedEffect(moreState.downloadedOnly || moreState.incognitoMode) {
        moreViewModel.state.collect {
          enableEdgeToEdge(
            statusBarStyle = if (moreState.downloadedOnly || moreState.incognitoMode) SystemBarStyle.light(
              Color.TRANSPARENT,
              Color.TRANSPARENT
            ) else SystemBarStyle.dark(
              Color.TRANSPARENT
            )
          )
        }
      }

      MaterialTheme(colorScheme = darkColorScheme(), shapes = Shapes()) {
        CompositionLocalProvider(
          GlobalValuesProvider provides
            GlobalValues(rememberNavController(), rememberNavController())
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
                modifier = Modifier
                  .align(Alignment.BottomCenter)
              )
            }
            NavHost(
              navController = GlobalValuesProvider.current.rootNavigator!!,
              startDestination = "home",
            ) {
              composable("home") { HomePage() }
              composable("settings") { SettingsPage() }
            }
          }
        }
      }
    }
  }
}
