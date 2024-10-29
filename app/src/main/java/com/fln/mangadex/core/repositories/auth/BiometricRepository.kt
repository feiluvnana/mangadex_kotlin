package com.fln.mangadex.core.repositories.auth

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.fln.mangadex.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BiometricModule {
  @Provides
  @Singleton
  fun provideBiometricRepository(): BiometricRepository {
    return BiometricRepository()
  }
}

class BiometricRepository {
  companion object {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val MainActivity.km: KeyguardManager
      get() = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    private val MainActivity.bm: BiometricManager
      get() = BiometricManager.from(this)
  }

  fun authenticate(
    activity: MainActivity,
    onSuccess: () -> Unit
  ) {
    try {
      val canAuthenticateByBiometric =
        activity.bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
      val canAuthenticateByKeyguard = activity.km.isDeviceSecure

      if (!canAuthenticateByBiometric && !canAuthenticateByKeyguard) {
        Toast.makeText(
          activity,
          "You must have Biometric or Device Credential to use this function.",
          Toast.LENGTH_SHORT
        )
          .show()
        return
      }

      if (!canAuthenticateByBiometric) {
        authenticateByKeyguardManager(activity, onSuccess)
        return
      }
      
      authenticateByBiometricPrompt(activity, onSuccess)
    } catch (e: Exception) {
      Toast.makeText(activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
  }

  private fun authenticateByBiometricPrompt(
    activity: MainActivity,
    onSuccess: () -> Unit
  ) {
    val canAuthenticateByKeyguard = activity.km.isDeviceSecure
    val executor = ContextCompat.getMainExecutor(activity)
    val callback = object : BiometricPrompt.AuthenticationCallback() {
      override fun onAuthenticationError(
        errorCode: Int,
        errString: CharSequence
      ) {
        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON && canAuthenticateByKeyguard) {
          authenticateByKeyguardManager(activity, onSuccess)
          return
        }
      }

      override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        onSuccess()
      }
    }
    val biometricPrompt = BiometricPrompt(activity, executor, callback)
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
      .setTitle("Require unlock")
      .setSubtitle("Authenticate to continue")
      .setNegativeButtonText(if (canAuthenticateByKeyguard) "Use password instead" else "Cancel")
      .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
      .build()
    biometricPrompt.authenticate(promptInfo)
  }

  private fun authenticateByKeyguardManager(
    activity: MainActivity,
    onSuccess: () -> Unit
  ) {
    val intent =
      activity.km.createConfirmDeviceCredentialIntent(
        "Require unlock",
        "Please enter your password"
      )
    coroutineScope.launch {
      val result = activity.startActivityResultFlow.first()
      if (result?.resultCode == Activity.RESULT_OK) {
        onSuccess()
      }
    }
    activity.startActivityForResultLauncher.launch(intent)

  }
}