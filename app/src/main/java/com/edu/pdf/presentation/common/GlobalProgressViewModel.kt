package com.edu.pdf.presentation.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.edu.pdf.worker.MoveWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class GlobalProgressUiState(
    val isVisible: Boolean = false,
    val current: Int = 0,
    val total: Int = 0,
    val isConfirmingCancel: Boolean = false,
    val targetPath: String? = null
)

sealed interface GlobalProgressEvent {
    data class OperationFinished(val targetPath: String?) : GlobalProgressEvent
}

@HiltViewModel
class GlobalProgressViewModel @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext context: Context
) : ViewModel() {

    private val workManager = WorkManager.getInstance(context)
    private val _isConfirmingCancel = MutableStateFlow(false)
    
    private val _events = Channel<GlobalProgressEvent>()
    val events = _events.receiveAsFlow()

    // 🌟 MONITOR ALL MOVE TASKS & HANDLE AUTO-NAVIGATION
    val uiState: StateFlow<GlobalProgressUiState> = combine(
        workManager.getWorkInfosForUniqueWorkFlow("bulk_move_task"),
        _isConfirmingCancel
    ) { workInfos, isConfirming ->
        val info = workInfos.firstOrNull()
        
        if (info != null) {
            val targetPath = info.progress.getString(MoveWorker.KEY_TARGET_PATH)
            
            when (info.state) {
                WorkInfo.State.RUNNING, WorkInfo.State.ENQUEUED -> {
                    val current = info.progress.getInt(MoveWorker.KEY_PROGRESS_CURRENT, 0)
                    val total = info.progress.getInt(MoveWorker.KEY_PROGRESS_TOTAL, 100)
                    GlobalProgressUiState(
                        isVisible = true,
                        current = current,
                        total = total,
                        isConfirmingCancel = isConfirming,
                        targetPath = targetPath
                    )
                }
                WorkInfo.State.SUCCEEDED -> {
                    // 🌟 AUTO-NAVIGATE TRIGGER: Signal completion
                    _events.send(GlobalProgressEvent.OperationFinished(targetPath))
                    GlobalProgressUiState(isVisible = false)
                }
                else -> GlobalProgressUiState(isVisible = false)
            }
        } else {
            GlobalProgressUiState(isVisible = false)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GlobalProgressUiState())

    fun requestCancel() { _isConfirmingCancel.value = true }
    fun dismissCancel() { _isConfirmingCancel.value = false }
    fun confirmCancel() {
        workManager.cancelUniqueWork("bulk_move_task")
        _isConfirmingCancel.value = false
    }
}
