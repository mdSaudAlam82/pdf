package com.edu.pdf.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity(tableName = "search_history_table")
data class SearchHistoryEntity(
    @PrimaryKey val query: String,
    val timestamp: Long
)