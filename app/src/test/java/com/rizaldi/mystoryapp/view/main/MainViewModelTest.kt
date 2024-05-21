package com.rizaldi.mystoryapp.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.rizaldi.mystoryapp.DataDummy
import com.rizaldi.mystoryapp.MainDispatcherRule
import com.rizaldi.mystoryapp.adapter.ListStoryAdapter
import com.rizaldi.mystoryapp.data.UserRepository
import com.rizaldi.mystoryapp.data.response.StoryItem
import com.rizaldi.mystoryapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: UserRepository

    private val dummyStories = DataDummy.generateStories()

    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val data: PagingData<StoryItem> = StoryPagingSource.snapshot(dummyStories.listStory)
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()
        expectedStory.value = data
        Mockito.`when`(repository.getStoriesPaging()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(repository)
        val actualStory: PagingData<StoryItem> = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)



        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.listStory, differ.snapshot())
        Assert.assertEquals(dummyStories.listStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories.listStory[0], differ.snapshot()[0])

    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryItem> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<StoryItem>>()
        expectedQuote.value = data
        Mockito.`when`(repository.getStoriesPaging()).thenReturn(expectedQuote)
        val mainViewModel = MainViewModel(repository)
        val actualQuote: PagingData<StoryItem> = mainViewModel.story.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}


class StoryPagingSource : PagingSource<Int, LiveData<List<StoryItem>>>() {
    companion object {
        fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}