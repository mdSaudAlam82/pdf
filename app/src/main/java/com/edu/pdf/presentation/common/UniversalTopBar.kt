package com.edu.pdf.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalTopBar(
    title: String,
    isGridView: Boolean = false,
    showSearch: Boolean = true,
    showCreateFolder: Boolean = false,
    showSort: Boolean = false,
    showToggleView: Boolean = true,
    showSelectAll: Boolean = true,
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
            // 🌟 SMOOTH ANIMATED ICONS: Sliding and Fading
            AnimatedVisibility(
                visible = showSearch && onSearchClick != null,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                IconButton(onClick = onSearchClick!!) { Icon(Icons.Default.Search, "Search") }
            }

            AnimatedVisibility(
                visible = showCreateFolder && onCreateFolderClick != null,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                IconButton(onClick = onCreateFolderClick!!) { Icon(Icons.Default.CreateNewFolder, "Create Folder") }
            }

            AnimatedVisibility(
                visible = showSort && onSortClick != null,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                IconButton(onClick = onSortClick!!) { Icon(Icons.AutoMirrored.Filled.Sort, "Sort") }
            }

            AnimatedVisibility(
                visible = showToggleView && onToggleView != null,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                IconButton(onClick = onToggleView!!) {
                    Icon(if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView, "Toggle View")
                }
            }

            AnimatedVisibility(
                visible = showSelectAll && onSelectAllClick != null,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                IconButton(onClick = onSelectAllClick!!) { Icon(Icons.Outlined.CheckBox, "Select Files") }
            }
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