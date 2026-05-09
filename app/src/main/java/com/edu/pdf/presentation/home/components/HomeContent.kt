package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edu.pdf.domain.model.HomeItem
import com.edu.pdf.presentation.home.HomeAction
import com.edu.pdf.presentation.home.HomeSheetState
import com.edu.pdf.presentation.home.HomeUiState
import kotlinx.collections.immutable.PersistentSet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeUiState,
    isRefreshing: Boolean,
    isSelectionMode: Boolean,
    selectedPdfs: PersistentSet<String>,
    paddingValues: PaddingValues,
    pagerState: PagerState, // 🌟 ViewModel se receive kiya hua state
    onAction: (HomeAction) -> Unit,
    onToggleSelection: (String) -> Unit,
    onSelectionModeChange: (Boolean) -> Unit
) {
    val onLongPressEnableSelection: (String) -> Unit = { id ->
        if (!isSelectionMode) {
            onSelectionModeChange(true)
            if (!selectedPdfs.contains(id)) onToggleSelection(id)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding())) {

        // 🌟 YEHAN SE 'HomeTabs' HATA DIYE GAYE HAIN KYUNKI WO UPAR SCROLL BAR ME CHIPAK GAYE HAIN

        if (state.breadcrumbs.isNotEmpty()) {
            com.edu.pdf.presentation.common.PremiumBreadcrumbs(
                breadcrumbs = state.breadcrumbs,
                onNavigate = {
                    // Root jane ke liye sab pop kardo
                    while(state.breadcrumbs.isNotEmpty()) onAction(HomeAction.NavigateUp)
                }
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            userScrollEnabled = !isSelectionMode && state.breadcrumbs.isEmpty()
        ) { page ->
            PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = { onAction(HomeAction.RefreshData) }) {
                val currentList = if (state.breadcrumbs.isNotEmpty()) {
                    state.currentFolderItems
                } else {
                    when (page) {
                        0 -> state.recentItems
                        1 -> state.currentFolderItems
                        else -> state.favoritePdfs.map { HomeItem.PdfItem(it) }
                    }
                }

                if (currentList.isEmpty()) {
                    EmptyStateView(title = "No Items Here", subtitle = "Start by creating a folder or adding PDFs.")
                } else {
                    if (state.isGridView) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 110.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(currentList, key = { it.id }) { item -> UnifiedGridItem(item, isSelectionMode, selectedPdfs, onAction, onToggleSelection, onLongPressEnableSelection) }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
                            items(currentList, key = { it.id }) { item -> UnifiedListItem(item, isSelectionMode, selectedPdfs, onAction, onToggleSelection, onLongPressEnableSelection) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UnifiedGridItem(item: HomeItem, isSelectionMode: Boolean, selectedPdfs: PersistentSet<String>, onAction: (HomeAction) -> Unit, onToggleSelection: (String) -> Unit, onLongPress: (String) -> Unit) {
    val isSelected = selectedPdfs.contains(item.id)
    when (item) {
        is HomeItem.FolderItem -> HomeFolderGridItem(folder = item.folder, isSelectionMode = isSelectionMode, isSelected = isSelected, onClick = { if (isSelectionMode) onToggleSelection(item.id) else onAction(
            HomeAction.NavigateToVirtualFolder(item.folder)) }, onLongClick = { onLongPress(item.id) }, onMoreOptionsClick = { onAction(
            HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) })
        is HomeItem.PdfItem -> PdfGridItem(pdf = item.pdf, isSelectionMode = isSelectionMode, isSelected = isSelected, onClick = { if (isSelectionMode) onToggleSelection(item.id) else onAction(
            HomeAction.ValidateAndOpenPdf(item.pdf)) }, onLongClick = { onLongPress(item.id) }, onMoreOptionsClick = { onAction(
            HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) })
    }
}

@Composable
fun UnifiedListItem(item: HomeItem, isSelectionMode: Boolean, selectedPdfs: PersistentSet<String>, onAction: (HomeAction) -> Unit, onToggleSelection: (String) -> Unit, onLongPress: (String) -> Unit) {
    val isSelected = selectedPdfs.contains(item.id)
    when (item) {
        is HomeItem.FolderItem -> HomeFolderListItem(folder = item.folder, isSelectionMode = isSelectionMode, isSelected = isSelected, onClick = { if (isSelectionMode) onToggleSelection(item.id) else onAction(
            HomeAction.NavigateToVirtualFolder(item.folder)) }, onLongClick = { onLongPress(item.id) }, onMoreOptionsClick = { onAction(
            HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) })
        is HomeItem.PdfItem -> PdfListItem(pdf = item.pdf, isSelectionMode = isSelectionMode, isSelected = isSelected, onClick = { if (isSelectionMode) onToggleSelection(item.id) else onAction(
            HomeAction.ValidateAndOpenPdf(item.pdf)) }, onLongClick = { onLongPress(item.id) }, onMoreOptionsClick = { onAction(
            HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) })
    }
}