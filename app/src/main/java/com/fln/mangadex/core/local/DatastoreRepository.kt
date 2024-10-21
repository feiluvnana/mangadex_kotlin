package com.fln.mangadex.core.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {
  @Provides
  @Singleton
  fun provideDatastoreRepository(
    @ApplicationContext context: Context
  ): DatastoreRepository {
    return DatastoreRepository(context)
  }
}

class DatastoreRepository
@Inject
constructor(@ApplicationContext private val context: Context) {
  companion object {
    private val Context.dataStore: DataStore<Preferences> by
      preferencesDataStore(name = "mangadex_datastore")
    val DOWNLOADED_ONLY = booleanPreferencesKey("downloaded_only")
    val INCOGNITO_MODE = booleanPreferencesKey("incognito_mode")
  }

  suspend fun <T> set(key: Preferences.Key<T>, value: T) {
    context.dataStore.edit { it[key] = value }
  }

  fun <T> get(key: Preferences.Key<T>): Flow<T?> {
    return context.dataStore.data.map { it[key] }
  }
}
