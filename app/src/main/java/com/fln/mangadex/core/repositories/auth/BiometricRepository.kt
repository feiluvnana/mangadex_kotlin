package com.fln.mangadex.core.repositories.auth

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Build.VERSION
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
import kotlinx.coroutines.flow.takeWhile
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
  fun authenticate(
    activity: MainActivity,
    onSuccess: () -> Unit
  ) {
    if (VERSION.SDK_INT <= 29) authenticateForApi29AndLower(activity, onSuccess)
  }

  private fun authenticateForApi29AndLower(
    activity: MainActivity,
    onSuccess: () -> Unit
  ) {
    try {
      val coroutineScope = CoroutineScope(Dispatchers.Main)
      val executor = ContextCompat.getMainExecutor(activity)
      val biometricManager = BiometricManager.from(activity)
      val canAuthenticateByBiometric =
        biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
      val keyguardManager =
        activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
      val canAuthenticateByKeyguard = keyguardManager.isDeviceSecure
      when (canAuthenticateByBiometric) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
          val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
              errorCode: Int,
              errString: CharSequence
            ) {
              if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON && canAuthenticateByKeyguard) {
                val intent =
                  keyguardManager.createConfirmDeviceCredentialIntent(
                    "Require unlock",
                    "Please enter your password"
                  )
                activity.startActivityForResultLauncher.launch(intent)
                val flow = activity.startActivityResultFlow.takeWhile { false }
                coroutineScope.launch {
                  flow.collect {
                    if (it?.resultCode == Activity.RESULT_OK) onSuccess()
                  }
                }
              }
              Toast.makeText(activity, errString, Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
              onSuccess()
            }

            override fun onAuthenticationFailed() {
              Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
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

        else -> {
          Toast.makeText(activity, "Cannot use biometrics", Toast.LENGTH_SHORT)
            .show()
        }
      }
    } catch (e: Exception) {
      Toast.makeText(activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
  }
}