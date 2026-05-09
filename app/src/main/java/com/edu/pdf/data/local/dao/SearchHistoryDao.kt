package com.edu.pdf.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.pdf.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Upsert
    suspend fun insertSearchQuery(searchHistory: SearchHistoryEntity)

    @Query("SELECT * FROM search_history_table ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history_table")
    suspend fun clearAllHistory()

    @Query("DELETE FROM search_history_table WHERE `query` = :query")
    suspend fun deleteSearchQuery(query: String)
}