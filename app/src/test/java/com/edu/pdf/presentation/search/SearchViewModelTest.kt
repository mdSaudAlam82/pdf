package com.edu.pdf.presentation.search

import com.edu.pdf.domain.model.PdfFile
import com.edu.pdf.domain.repository.PdfRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var repository: PdfRepository
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // ViewModel ke tests ke liye Dispatcher set karna padta hai
        Dispatchers.setMain(testDispatcher)

        repository = mockk(relaxed = true)
        
        // Shuruat mein history khali dikhao
        every { repository.getRecentSearchQueries() } returns flowOf(emptyList())

        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        // Test khatam hone par wapas reset kar do
        Dispatchers.resetMain()
    }

    @Test
    fun `jab user query badle, toh state mein query update honi chahiye`() = runTest {
        // Arrange: Coroutine ko start hone ka mauka do
        testDispatcher.scheduler.advanceUntilIdle()

        // Act: User ne search bar mein "physics" likha
        val newQuery = "physics"
        viewModel.onAction(SearchAction.OnQueryChange(newQuery))

        // Assert: Thoda intezar karo taaki Flow update ho jaye
        testDispatcher.scheduler.advanceTimeBy(400)
        
        val state = viewModel.uiState.value
        assertEquals(newQuery, state.query)
    }

    @Test
    fun `jab search query badle, toh repository se results aane chahiye`() = runTest {
        // Arrange: Fake data tayyar karo
        val query = "bill"
        val fakeResults = listOf(
            PdfFile(id = "1", name = "bill_jan.pdf", path = "/a", sizeInBytes = 10, lastModified = 0)
        )
        
        // Repository ko bolo ki jab "bill" search ho toh ye fake list dena
        every { repository.searchPdfs(query) } returns flowOf(fakeResults)

        // Act: Search query badlo
        viewModel.onAction(SearchAction.OnQueryChange(query))

        // Thoda intezar karo (kyunki ViewModel mein debounce laga hai)
        testDispatcher.scheduler.advanceTimeBy(400) 

        // Assert: Check karo ki results screen par dikh rahe hain
        val state = viewModel.uiState.first { it.results.isNotEmpty() }
        assertEquals(1, state.results.size)
        assertEquals("bill_jan.pdf", state.results[0].name)
    }
}