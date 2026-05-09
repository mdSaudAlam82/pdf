package com.edu.pdf.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "pdf_user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val IS_GRID_VIEW = booleanPreferencesKey("is_grid_view")
        val IS_FOLDER_GRID_VIEW = booleanPreferencesKey("is_folder_grid_view")
        val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
        val IS_INITIAL_SCAN_COMPLETED = booleanPreferencesKey("is_initial_scan_completed")
    }

    val isGridViewFlow: Flow<Boolean> = dataStore.data.map { it[IS_GRID_VIEW] ?: false }
    val isFolderGridViewFlow: Flow<Boolean> = dataStore.data.map { it[IS_FOLDER_GRID_VIEW] ?: false }

    suspend fun saveGridViewPreference(isGrid: Boolean) = dataStore.edit { it[IS_GRID_VIEW] = isGrid }
    suspend fun saveFolderGridViewPreference(isGrid: Boolean) = dataStore.edit { it[IS_FOLDER_GRID_VIEW] = isGrid }

    suspend fun getLastSyncTime(): Long = dataStore.data.map { it[LAST_SYNC_TIME] ?: 0L }.first()
    suspend fun updateLastSyncTime(time: Long) = dataStore.edit { it[LAST_SYNC_TIME] = time }

    suspend fun isInitialScanCompleted(): Boolean = dataStore.data.map { it[IS_INITIAL_SCAN_COMPLETED] ?: false }.first()
    suspend fun setInitialScanCompleted(completed: Boolean) = dataStore.edit { it[IS_INITIAL_SCAN_COMPLETED] = completed }
}