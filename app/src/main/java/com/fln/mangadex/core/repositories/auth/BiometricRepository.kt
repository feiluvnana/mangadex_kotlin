package com.fln.mangadex.core.repositories.auth

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
object BiometricModule {
  @Provides
  fun provideBiometricRepository(fragment: FragmentActivity): BiometricRepository {
    return BiometricRepository(fragment)
  }
}

class BiometricRepository @Inject constructor(private val fragment: FragmentActivity) {
  val biometricPrompt = BiometricPrompt(
    fragment,
    object : BiometricPrompt.AuthenticationCallback() {})
  val biometricPromptInfo =
    BiometricPrompt.PromptInfo.Builder().setTitle("Require unlock")
      .setSubtitle("Authenticate to confirm change").build()
}