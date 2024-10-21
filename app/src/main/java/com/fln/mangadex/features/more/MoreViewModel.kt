package com.fln.mangadex.features.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fln.mangadex.core.local.DatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoreState(val downloadedOnly: Boolean, val incognitoMode: Boolean)

@HiltViewModel
class MoreViewModel
@Inject
constructor(private val datastoreService: DatastoreRepository) : ViewModel() {
  private val _state =
    MutableStateFlow(MoreState(downloadedOnly = false, incognitoMode = false))
  val state: StateFlow<MoreState>
    get() = _state.asStateFlow()

  init {
    viewModelScope.launch {
      combine(
          datastoreService.get(DatastoreRepository.DOWNLOADED_ONLY),
          datastoreService.get(DatastoreRepository.INCOGNITO_MODE),
        ) { downloadedOnly, incognitoMode ->
          MoreState(
            downloadedOnly = downloadedOnly == true,
            incognitoMode = incognitoMode == true,
          )
        }
        .collect { newState -> _state.value = newState }
    }
  }

  suspend fun toggleDownloadedOnly() {
    datastoreService.set(
      DatastoreRepository.DOWNLOADED_ONLY,
      !state.value.downloadedOnly,
    )
  }

  suspend fun toggleIncognitoMode() {
    datastoreService.set(
      DatastoreRepository.INCOGNITO_MODE,
      !state.value.incognitoMode,
    )
  }
}
