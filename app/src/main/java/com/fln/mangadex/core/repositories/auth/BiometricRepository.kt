package com.fln.mangadex.core.repositories.auth

import android.app.Activity
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.fln.mangadex.BiometricActivity
import com.fln.mangadex.utils.bm
import com.fln.mangadex.utils.km
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch


object BiometricRepository {
  fun authenticate(
    onCancelled: () -> Unit,
    onSuccess: () -> Unit,
    activity: BiometricActivity
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
        authenticateByKeyguardManager(onCancelled, onSuccess, activity)
        return
      }

      authenticateByBiometricPrompt(onCancelled, onSuccess, activity)
    } catch (e: Exception) {
      Toast.makeText(activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
  }

  private fun authenticateByBiometricPrompt(
    onCancelled: () -> Unit,
    onSuccess: () -> Unit,
    activity: BiometricActivity
  ) {
    val canAuthenticateByKeyguard = activity.km.isDeviceSecure
    val executor = ContextCompat.getMainExecutor(activity)
    val callback = object : BiometricPrompt.AuthenticationCallback() {
      override fun onAuthenticationError(
        errorCode: Int,
        errString: CharSequence
      ) {
        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON && canAuthenticateByKeyguard) {
          authenticateByKeyguardManager(onCancelled, onSuccess, activity)
          return
        } else if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
          onCancelled()
          return
        } else if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
          onCancelled()
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
    onCancelled: () -> Unit,
    onSuccess: () -> Unit,
    activity: BiometricActivity
  ) {
    activity.lifecycleScope.launch {
      val intent =
        activity.km.createConfirmDeviceCredentialIntent(
          "Require unlock",
          "Please enter your password"
        )
      activity.startActivityForResultLauncher.launch(intent)
      activity.startActivityResultFlow.drop(1).collect {
        if (it?.resultCode == Activity.RESULT_OK) {
          onSuccess()
        } else if (it?.resultCode == Activity.RESULT_CANCELED) {
          onCancelled()
        }
      }
    }
  }
}