package com.edu.pdf.presentation.core

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SelectionManagerTest {

    private lateinit var selectionManager: SelectionManager

    @Before
    fun setup() {
        selectionManager = SelectionManager()
    }

    @Test
    fun `toggleSelection should add id if not present`() = runTest {
        selectionManager.toggleSelection("id1")
        val state = selectionManager.selectionState.first()
        assertTrue(state.selectedIds.contains("id1"))
        assertTrue(state.isSelectionMode)
    }

    @Test
    fun `toggleSelection should remove id if present`() = runTest {
        selectionManager.toggleSelection("id1")
        selectionManager.toggleSelection("id1")
        val state = selectionManager.selectionState.first()
        assertFalse(state.selectedIds.contains("id1"))
        assertFalse(state.isSelectionMode)
    }

    @Test
    fun `selectAll should add all ids`() = runTest {
        val ids = listOf("id1", "id2", "id3")
        selectionManager.selectAll(ids)
        val state = selectionManager.selectionState.first()
        assertEquals(3, state.selectedIds.size)
        assertTrue(state.selectedIds.containsAll(ids))
        assertTrue(state.isSelectionMode)
    }

    @Test
    fun `clearSelection should reset state`() = runTest {
        selectionManager.toggleSelection("id1")
        selectionManager.clearSelection()
        val state = selectionManager.selectionState.first()
        assertTrue(state.selectedIds.isEmpty())
        assertFalse(state.isSelectionMode)
    }
}
