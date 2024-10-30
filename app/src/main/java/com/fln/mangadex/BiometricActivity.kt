package com.fln.mangadex

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.fln.mangadex.core.repositories.auth.BiometricRepository
import com.fln.mangadex.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BiometricActivity : FragmentActivity() {
  val startActivityResultFlow: MutableStateFlow<ActivityResult?> =
    MutableStateFlow(null)
  val startActivityForResultLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) {
    lifecycleScope.launch {
      startActivityResultFlow.emit(it)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    BiometricRepository.authenticate(
      onCancelled = {
        setResult(RESULT_CANCELED)
        finish()
      },
      onSuccess = {
        setResult(RESULT_OK)
        finish()
      }, this
    )
    Thread.setDefaultUncaughtExceptionHandler { _, _ -> }
    setContent {
      AppTheme {
        Surface { }
      }
    }
  }
}