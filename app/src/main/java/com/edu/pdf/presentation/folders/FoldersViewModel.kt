package com.edu.pdf.presentation.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.pdf.domain.model.SortType
import com.edu.pdf.domain.repository.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File
import javax.inject.Inject

data class DeviceFolder(val name: String, val absolutePath: String, val pdfCount: Int)

@HiltViewModel
class FoldersViewModel @Inject constructor(repository: PdfRepository) : ViewModel() {
    // 🌟 Sirf physical device folders nikalenge (WhatsApp, Downloads etc.)
    val deviceFolders = repository.getAllPdfs(SortType.NAME_ASC).map { pdfs ->
        pdfs.groupBy { File(it.path).parentFile?.absolutePath ?: "Unknown" }
            .map { (path, list) -> DeviceFolder(File(path).name, path, list.size) }
            .sortedBy { it.name.lowercase() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}