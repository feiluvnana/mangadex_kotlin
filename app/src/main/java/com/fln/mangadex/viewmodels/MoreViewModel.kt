package com.fln.mangadex.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fln.mangadex.core.models.SecureScreenMode
import com.fln.mangadex.core.repositories.auth.BiometricRepository
import com.fln.mangadex.core.repositories.local.DatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoreState(
  val downloadedOnly: Boolean,
  val incognitoMode: Boolean,
  val secureScreen: SecureScreenMode,
  val requireUnlock: Boolean
)

@HiltViewModel
class MoreViewModel
@Inject constructor(
  private val datastoreService: DatastoreRepository,
  val biometricRepository: BiometricRepository
) :
  ViewModel() {
  private val _state = MutableStateFlow(
    MoreState(
      downloadedOnly = false,
      incognitoMode = false,
      secureScreen = SecureScreenMode.Never,
      requireUnlock = false
    )
  )
  val state: StateFlow<MoreState>
    get() = _state.asStateFlow()

  init {
    viewModelScope.launch {
      combine(
        datastoreService.get(DatastoreRepository.DOWNLOADED_ONLY),
        datastoreService.get(DatastoreRepository.INCOGNITO_MODE),
        datastoreService.get(DatastoreRepository.SECURE_SCREEN),
        datastoreService.get(DatastoreRepository.REQUIRE_UNLOCK)
      ) { downloadedOnly, incognitoMode, secureScreen, requireUnlock ->
        MoreState(
          downloadedOnly = downloadedOnly == true,
          incognitoMode = incognitoMode == true,
          secureScreen = SecureScreenMode.valueOf(secureScreen ?: "Never"),
          requireUnlock = requireUnlock == true
        )
      }.collect { newState -> _state.value = newState }
    }
  }

  suspend fun toggleDownloadedOnly() {
    datastoreService.set(
      DatastoreRepository.DOWNLOADED_ONLY,
      !state.value.downloadedOnly
    )
  }

  suspend fun toggleIncognitoMode() {
    datastoreService.set(
      DatastoreRepository.INCOGNITO_MODE,
      !state.value.incognitoMode
    )
  }

  suspend fun setSecureScreen(mode: SecureScreenMode) {
    datastoreService.set(DatastoreRepository.SECURE_SCREEN, mode.name)
  }

  suspend fun toggleRequireUnlock() {
    datastoreService.set(
      DatastoreRepository.REQUIRE_UNLOCK,
      !state.value.requireUnlock
    )
  }
}