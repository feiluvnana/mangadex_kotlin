package com.fln.mangadex.utils

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricManager
import com.fln.mangadex.MainActivity

fun Context.getActivity(): MainActivity {
  return when (this) {
    is ComponentActivity -> this as MainActivity
    is ContextWrapper -> baseContext.getActivity()
    else -> throw Error("Invalid Context")
  }
}

val Activity.km: KeyguardManager
  get() = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
val Activity.bm: BiometricManager
  get() = BiometricManager.from(this)