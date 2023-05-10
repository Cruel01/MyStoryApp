package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.DummyTarget
import com.example.storyapp.MainDispatcher
import com.example.storyapp.adapter.HomeAdapter
import com.example.storyapp.data.Story
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.viewModel.IRepository
import com.example.storyapp.viewModel.Repository
import com.example.storyapp.viewModel.UniversalVM
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UniversalVMTest {

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val mainDispatcherRules = MainDispatcher()

    @Mock
    private lateinit var repository: IRepository

    @Test
    fun `The Story must Not Null and Return Expected Data`() = runTest {
        val dummyStories = DummyTarget.generateDummyStoriesResponse()
        val data: PagingData<Story> = StoriesPagingSource.snapshot(dummyStories)
        val expectedData = MutableLiveData<PagingData<Story>>()
        expectedData.value = data
        `when`(repository.getAllStory()).thenReturn(expectedData)

        val viewModel = UniversalVM(repository)
        val actualStories: PagingData<Story> = viewModel.getAllStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = HomeAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `The Story Empty adn Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        `when`(repository.getAllStory()).thenReturn(expectedStories)

        val viewModel = UniversalVM(repository)
        val actualStories: PagingData<Story> = viewModel.getAllStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = HomeAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        Assert.assertEquals(0, differ.snapshot().size)
    }

}

class StoriesPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
