package com.fln.mangadex.core.repositories.auth

import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
  fun authenticate(activity: FragmentActivity) {
    try {
      Log.d("BiometricDebug", "Starting authentication process")

      val executor = ContextCompat.getMainExecutor(activity)
      Log.d("BiometricDebug", "Executor created")

      // Check biometric availability
      val biometricManager = BiometricManager.from(activity)
      val canAuthenticate =
        biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)

      Log.d("BiometricDebug", "Can authenticate result: $canAuthenticate")

      when (canAuthenticate) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
          Log.d("BiometricDebug", "Biometric is available")

          val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
              errorCode: Int,
              errString: CharSequence
            ) {
              super.onAuthenticationError(errorCode, errString)
              Log.d("BiometricDebug", "Error: $errorCode - $errString")
              activity.runOnUiThread {
                Toast.makeText(activity, errString, Toast.LENGTH_SHORT).show()
              }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
              super.onAuthenticationSucceeded(result)
              Log.d("BiometricDebug", "Authentication succeeded")
              activity.runOnUiThread {
                Toast.makeText(activity, "Success!", Toast.LENGTH_SHORT).show()
              }
            }

            override fun onAuthenticationFailed() {
              super.onAuthenticationFailed()
              Log.d("BiometricDebug", "Authentication failed")
              activity.runOnUiThread {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
              }
            }
          }

          Log.d("BiometricDebug", "Creating BiometricPrompt")
          val biometricPrompt = BiometricPrompt(activity, executor, callback)

          Log.d("BiometricDebug", "Creating PromptInfo")
          val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate to continue")
            .setNegativeButtonText("Use password instead")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

          Log.d("BiometricDebug", "Calling authenticate()")
          biometricPrompt.authenticate(promptInfo)
          Log.d("BiometricDebug", "authenticate() called")
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
          Log.e("BiometricDebug", "No biometric hardware")
          Toast.makeText(activity, "No biometric hardware", Toast.LENGTH_SHORT)
            .show()
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
          Log.e("BiometricDebug", "No biometrics enrolled")
          Toast.makeText(
            activity,
            "No biometrics enrolled. Please enroll in Settings",
            Toast.LENGTH_LONG
          ).show()
        }

        else -> {
          Log.e("BiometricDebug", "Biometric error: $canAuthenticate")
          Toast.makeText(activity, "Cannot use biometrics", Toast.LENGTH_SHORT)
            .show()
        }
      }
    } catch (e: Exception) {
      Log.e("BiometricDebug", "Exception during authentication", e)
      Toast.makeText(activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
  }
}