package life.sabujak.pickle.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagedList
import androidx.test.core.app.ApplicationProvider
import life.sabujak.pickle.LoggingObserver
import life.sabujak.pickle.data.cursor.StubCursorFactory
import life.sabujak.pickle.data.entity.PickleItem
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PickleMediaRepositoryTest {

    private lateinit var context: Context
    private lateinit var cursorFactory: StubCursorFactory
    private lateinit var repository: MediaRepository

    @Suppress("unused")
    @get:Rule // LiveData가 동기적으로 작업을 하도록 함
    val instantExecutor = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        print("setUp")
        this.cursorFactory = StubCursorFactory()
        this.context = ApplicationProvider.getApplicationContext()
        this.repository = PickleMediaRepository(context, cursorFactory)

    }

    @Test
    fun getPagedList() {
        cursorFactory.setup100Images()
        val observer =
            LoggingObserver<PagedList<PickleItem>>()
        val pagedList = repository.queryMediaList()
        assertNotNull(pagedList)
        pagedList.observeForever(observer)
        assertEquals(100, observer.value?.size)
    }

    @Test
    fun emptyList(){
        cursorFactory.clear()
        val observer =
            LoggingObserver<PagedList<PickleItem>>()
        val pagedList = repository.queryMediaList()
        assertNotNull(pagedList)
        pagedList.observeForever(observer)
        assertEquals(0, observer.value?.size)
    }

}