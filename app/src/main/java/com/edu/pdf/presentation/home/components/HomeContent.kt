package com.edu.pdf.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.itemKey
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
    pagerState: PagerState,
    onAction: (HomeAction) -> Unit,
    onToggleSelection: (String) -> Unit,
    onSelectionModeChange: (Boolean) -> Unit,
    pagedPdfs: androidx.paging.compose.LazyPagingItems<HomeItem.PdfItem>,
) {
    val onLongPressEnableSelection: (String) -> Unit = { id ->
        if (!isSelectionMode) {
            onSelectionModeChange(true)
            if (!selectedPdfs.contains(id)) onToggleSelection(id)
        }
    }
    androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onAction(HomeAction.ImportFile(it.toString())) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding())) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            userScrollEnabled = !isSelectionMode
        ) { page ->
            PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = { onAction(HomeAction.RefreshData) }) {

                // 🌟 MVI STRICT FIX: All Files (Page 1) me ab Paging use hoga
                if (page == 1) {
                    if (state.currentFolders.isEmpty() && pagedPdfs.itemCount == 0) {
                        // 🌟 PREMIUM TEXT FOR 'ALL FILES'
                        EmptyStateView(
                            title = "Your Workspace is Empty",
                            subtitle = "Start building your library. Import PDFs or create folders to get organized."
                        )
                    } else {
                        if (state.isGridView) {
                            key(state.sortType) {
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(minSize = 110.dp),
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    items(state.currentFolders, key = { it.id }) { item ->
                                        UnifiedGridItem(
                                            item = item, 
                                            isSelectionMode = isSelectionMode, 
                                            selectedPdfs = selectedPdfs, 
                                            onAction = onAction, 
                                            onToggleSelection = onToggleSelection, 
                                            onLongPress = onLongPressEnableSelection,
                                            modifier = Modifier.animateItem()
                                        )
                                    }
                                    items(count = pagedPdfs.itemCount, key = pagedPdfs.itemKey { it.pdf.id }) { index ->
                                        val pdfItem = pagedPdfs[index]
                                        if (pdfItem != null) UnifiedGridItem(
                                            item = pdfItem, 
                                            isSelectionMode = isSelectionMode, 
                                            selectedPdfs = selectedPdfs, 
                                            onAction = onAction, 
                                            onToggleSelection = onToggleSelection, 
                                            onLongPress = onLongPressEnableSelection,
                                            modifier = Modifier.animateItem()
                                        )
                                    }
                                }
                            }
                        } else {
                            key(state.sortType) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 120.dp)
                                ) {
                                    items(state.currentFolders, key = { it.id }) { item ->
                                        UnifiedListItem(
                                            item = item, 
                                            isSelectionMode = isSelectionMode, 
                                            selectedPdfs = selectedPdfs, 
                                            onAction = onAction, 
                                            onToggleSelection = onToggleSelection, 
                                            onLongPress = onLongPressEnableSelection,
                                            modifier = Modifier.animateItem()
                                        )
                                    }
                                    items(count = pagedPdfs.itemCount, key = pagedPdfs.itemKey { it.pdf.id }) { index ->
                                        val pdfItem = pagedPdfs[index]
                                        if (pdfItem != null) UnifiedListItem(
                                            item = pdfItem, 
                                            isSelectionMode = isSelectionMode, 
                                            selectedPdfs = selectedPdfs, 
                                            onAction = onAction, 
                                            onToggleSelection = onToggleSelection, 
                                            onLongPress = onLongPressEnableSelection,
                                            modifier = Modifier.animateItem()
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Page 0 (Recent) aur Page 2 (Favorites) ka purana logic
                    val currentList = if (page == 0) state.recentItems else state.favoritePdfs.map { HomeItem.PdfItem(it) }

                    if (currentList.isEmpty()) {
                        // 🌟 PREMIUM SMART TEXT FOR 'RECENT' & 'FAVORITES'
                        if (page == 0) {
                            EmptyStateView(
                                title = "No Recent Activity",
                                subtitle = "Pick up right where you left off. Open any PDF to quickly access it here."
                            )
                        } else {
                            EmptyStateView(
                                title = "No Favorites Yet",
                                subtitle = "Keep your important PDFs handy. Tap the bookmark icon to add them here."
                            )
                        }
                    } else {
                        if (state.isGridView) {
                            key(state.sortType) {
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(minSize = 110.dp),
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 120.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    items(currentList, key = { it.id }) { item ->
                                        UnifiedGridItem(
                                            item = item, 
                                            isSelectionMode = isSelectionMode, 
                                            selectedPdfs = selectedPdfs, 
                                            onAction = onAction, 
                                            onToggleSelection = onToggleSelection, 
                                            onLongPress = onLongPressEnableSelection,
                                            modifier = Modifier.animateItem()
                                        )
                                    }
                                }
                            }
                        } else {
                            key(state.sortType) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 120.dp)
                                ) {
                                    items(currentList, key = { it.id }) { item ->
                                        UnifiedListItem(
                                            item = item, 
                                            isSelectionMode = isSelectionMode, 
                                            selectedPdfs = selectedPdfs, 
                                            onAction = onAction, 
                                            onToggleSelection = onToggleSelection, 
                                            onLongPress = onLongPressEnableSelection,
                                            modifier = Modifier.animateItem()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UnifiedGridItem(
    item: HomeItem, 
    isSelectionMode: Boolean, 
    selectedPdfs: PersistentSet<String>, 
    onAction: (HomeAction) -> Unit, 
    onToggleSelection: (String) -> Unit, 
    onLongPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = selectedPdfs.contains(item.id)
    Box(modifier = modifier) {
        when (item) {
            is HomeItem.FolderItem -> HomeFolderGridItem(
                folder = item.folder,
                isSelectionMode = isSelectionMode,
                isSelected = isSelected,
                onClick = {
                    if (isSelectionMode) onToggleSelection(item.id)
                    else onAction(HomeAction.NavigateToVirtualFolder(item.folder))
                },
                onLongClick = { onLongPress(item.id) },
                onMoreOptionsClick = {
                    onAction(HomeAction.OpenSheet(HomeSheetState.ItemMenu(item)))
                }
            )
            is HomeItem.PdfItem -> PdfGridItem(
                pdf = item.pdf, 
                isSelectionMode = isSelectionMode, 
                isSelected = isSelected, 
                onClick = { 
                    if (isSelectionMode) onToggleSelection(item.id) 
                    else onAction(HomeAction.ValidateAndOpenPdf(item.pdf)) 
                }, 
                onLongClick = { onLongPress(item.id) }, 
                onMoreOptionsClick = { 
                    onAction(HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) 
                }
            )
        }
    }
}

@Composable
fun UnifiedListItem(
    item: HomeItem, 
    isSelectionMode: Boolean, 
    selectedPdfs: PersistentSet<String>, 
    onAction: (HomeAction) -> Unit, 
    onToggleSelection: (String) -> Unit, 
    onLongPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = selectedPdfs.contains(item.id)
    Box(modifier = modifier) {
        when (item) {
            is HomeItem.FolderItem -> HomeFolderListItem(
                folder = item.folder,
                isSelectionMode = isSelectionMode,
                isSelected = isSelected,
                onClick = {
                    if (isSelectionMode) onToggleSelection(item.id)
                    else onAction(HomeAction.NavigateToVirtualFolder(item.folder))
                },
                onLongClick = { onLongPress(item.id) },
                onMoreOptionsClick = {
                    onAction(HomeAction.OpenSheet(HomeSheetState.ItemMenu(item)))
                }
            )
            is HomeItem.PdfItem -> PdfListItem(
                pdf = item.pdf, 
                isSelectionMode = isSelectionMode, 
                isSelected = isSelected, 
                onClick = { 
                    if (isSelectionMode) onToggleSelection(item.id) 
                    else onAction(HomeAction.ValidateAndOpenPdf(item.pdf)) 
                }, 
                onLongClick = { onLongPress(item.id) }, 
                onMoreOptionsClick = { 
                    onAction(HomeAction.OpenSheet(HomeSheetState.ItemMenu(item))) 
                }
            )
        }
    }
}
