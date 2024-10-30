package com.fln.mangadex.core.repositories.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {
  @Provides
  @Singleton
  fun provideDatastoreRepository(@ApplicationContext context: Context): DatastoreRepository {
    return DatastoreRepository(context)
  }
}

class DatastoreRepository
@Inject constructor(@ApplicationContext private val context: Context) {
  companion object {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
      name = "mangadex_datastore"
    )
    val DOWNLOADED_ONLY = booleanPreferencesKey("downloaded_only")
    val INCOGNITO_MODE = booleanPreferencesKey("incognito_mode")
    val SECURE_SCREEN = stringPreferencesKey("secure_screen")
    val REQUIRE_UNLOCK = booleanPreferencesKey("require_unlock")
    val LOCK_WHEN_IDLE = stringPreferencesKey("lock_when_idle")
  }

  suspend fun <T> set(key: Preferences.Key<T>, value: T) {
    context.dataStore.edit { it[key] = value }
  }

  fun <T> get(key: Preferences.Key<T>): Flow<T?> {
    return context.dataStore.data.map { it[key] }
  }
}
