package com.edu.pdf.presentation.core.components

import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptivePdfLayout(
    listContent: @Composable () -> Unit,
    detailContent: @Composable (String) -> Unit,
    selectedPdfPath: String?,
    onPdfSelected: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()
    val scope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane(modifier = Modifier) {
                listContent()
            }
        },
        detailPane = {
            AnimatedPane(modifier = Modifier) {
                selectedPdfPath?.let { path ->
                    detailContent(path)
                }
            }
        },
        modifier = modifier
    )

    // Sync navigator with selectedPdfPath
    androidx.compose.runtime.LaunchedEffect(selectedPdfPath) {
        if (selectedPdfPath != null) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, selectedPdfPath)
        } else if (navigator.canNavigateBack()) {
            navigator.navigateBack()
        }
    }
    
    androidx.activity.compose.BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
            onBack()
        }
    }
}
