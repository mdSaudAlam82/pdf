package com.edu.pdf.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalTopBar(
    title: String,
    currentTab: Int = 1,
    isGridView: Boolean = false,
    onSelectAllClick: (() -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    onSortClick: (() -> Unit)? = null,
    onToggleView: (() -> Unit)? = null,
    onCreateFolderClick: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            if (onSearchClick != null) IconButton(onClick = onSearchClick) { Icon(Icons.Default.Search, "Search") }
            if (onCreateFolderClick != null) IconButton(onClick = onCreateFolderClick) { Icon(Icons.Default.CreateNewFolder, "Create Folder") }
            if (onSortClick != null && currentTab != 0) IconButton(onClick = onSortClick) { Icon(Icons.AutoMirrored.Filled.Sort, "Sort") }
            if (onToggleView != null) IconButton(onClick = onToggleView) { Icon(if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView, "Toggle View") }
            if (onSelectAllClick != null) IconButton(onClick = onSelectAllClick) { Icon(Icons.Outlined.CheckBox, "Select Files") }
        },
        windowInsets = TopAppBarDefaults.windowInsets,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            // 🌟 PRO FIX: 100% Sheesha! Ab color parent Column se aayega.
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            scrolledContainerColor = androidx.compose.ui.graphics.Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}