package com.edu.pdf.presentation.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File
import javax.inject.Inject

data class DeviceFolder(val name: String, val absolutePath: String, val pdfCount: Int)

// 🌟 STRICT MVI: State Definition
data class FoldersUiState(
    val deviceFolders: ImmutableList<DeviceFolder> = persistentListOf(),
    val isLoading: Boolean = true
)

// 🌟 STRICT MVI: Actions (Currently basic, but ready to scale)
sealed interface FoldersAction {
    // Agar future me pull-to-refresh ya sort add karna ho to yahan aayega
    data object RefreshFolders : FoldersAction
}

@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val repository: PdfRepository
) : ViewModel() {

    // 🌟 SINGLE SOURCE OF TRUTH
    val uiState: StateFlow<FoldersUiState> = repository.getAllPdfs(SortType.NAME_ASC)
        .map { pdfs ->
            val groupedFolders = pdfs.groupBy { File(it.path).parentFile?.absolutePath ?: "Unknown" }
                .map { (path, list) -> DeviceFolder(File(path).name, path, list.size) }
                .sortedBy { it.name.lowercase() }
                .toImmutableList() // 🌟 Immutable List for Compose Performance

            FoldersUiState(
                deviceFolders = groupedFolders,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FoldersUiState(isLoading = true)
        )

    fun onAction(action: FoldersAction) {
        when (action) {
            is FoldersAction.RefreshFolders -> {
                // Future scalability: Jab pull-to-refresh add karoge
            }
        }
    }
}