package com.edu.pdf.presentation.settings

import com.edu.pdf.data.preferences.AiKeyManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private lateinit var keyManager: AiKeyManager
    private lateinit var viewModel: SettingsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        // KeyManager ko mock karo
        keyManager = mockk(relaxed = true)
        
        // Shuruat mein koi purani keys return karwao
        every { keyManager.getKeys() } returns listOf("old_key_1", "old_key_2", "old_key_3")

        viewModel = SettingsViewModel(keyManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `jab ViewModel bante hi purani keys load honi chahiye`() {
        // Assert
        val state = viewModel.state.value
        assertEquals("old_key_1", state.primaryKey)
        assertEquals("old_key_2", state.fallbackKey1)
        assertEquals("old_key_3", state.fallbackKey2)
    }

    @Test
    fun `jab user key badle, toh state mein key update honi chahiye`() {
        // Act: User ne naya key type kiya
        val newKey = "new_secret_key"
        viewModel.onAction(SettingsAction.UpdatePrimaryKey(newKey))

        // Assert
        assertEquals(newKey, viewModel.state.value.primaryKey)
    }

    @Test
    fun `jab primary key khali ho aur user save kare, toh error aana chahiye`() = runTest {
        // Arrange: Primary key ko khali kar do
        viewModel.onAction(SettingsAction.UpdatePrimaryKey(""))

        // Act: Save dabao
        viewModel.onAction(SettingsAction.SaveAndVerifyKeys)

        // Assert
        assertEquals("Primary Key is required!", viewModel.state.value.validationMessage)
        
        // Verify karo ki keyManager.saveKeys call nahi hua
        verify(exactly = 0) { keyManager.saveKeys(any(), any(), any()) }
    }
}