package com.fln.mangadex.common

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavigation = compositionLocalOf { FlnNavHostControllers() }

data class FlnNavHostControllers(
  val root: NavHostController? = null,
  val home: NavHostController? = null
)
