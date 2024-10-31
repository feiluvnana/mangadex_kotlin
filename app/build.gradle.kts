plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.compose.compiler)
  id("com.google.devtools.ksp")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.fln.mangadex"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.fln.mangadex"
    minSdk = 24
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"
    vectorDrawables { useSupportLibrary = true }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_20
    targetCompatibility = JavaVersion.VERSION_20
  }
  kotlinOptions { jvmTarget = "20" }
  buildFeatures { compose = true }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
}

dependencies {
  implementation(platform(libs.compose.bom))
  implementation(libs.ui)
  implementation(libs.material3)
  implementation(libs.ui.tooling)
  implementation(libs.runtime)

  implementation(libs.activity.compose)
  implementation(libs.navigation.compose)
  implementation(libs.datastore.preferences)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.material.icons.extended)
  implementation(libs.biometric)

  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}
java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(20)
  }
}
