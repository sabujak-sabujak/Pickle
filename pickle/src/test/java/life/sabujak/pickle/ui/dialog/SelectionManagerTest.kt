package life.sabujak.pickle.ui.dialog

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.test.core.app.ApplicationProvider
import life.sabujak.pickle.LoggingObserver
import life.sabujak.pickle.data.entity.Image
import life.sabujak.pickle.data.entity.Media
import life.sabujak.pickle.data.entity.PickleItem
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.stubbing.Answer
import org.robolectric.RobolectricTestRunner
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

@RunWith(RobolectricTestRunner::class)
class SelectionManagerTest {
    @Suppress("unused")
    @get:Rule // LiveData가 동기적으로 작업을 하도록 함
    val instantExecutor = InstantTaskExecutorRule()
    lateinit var selectionManager: SelectionManager
    private val ids = arrayOf(0L, 1L, 2L, 3L)

    @Before
    fun setup() {
        selectionManager = SelectionManager()
    }

    @Test
    fun setChecked() {
        for (id in ids) {
            selectionManager.setChecked(id, true)
        }
        for (id in ids) {
            assertTrue(selectionManager.selectedIds.contains(id))
        }
        for (id in ids) {
            selectionManager.setChecked(id, false)
        }
        for (id in ids) {
            assertFalse(selectionManager.selectedIds.contains(id))
        }
    }

    @Test
    fun isChecked() {
        for (id in ids) {
            selectionManager.setChecked(id, true)
        }
        for (id in ids) {
            assertTrue(selectionManager.isChecked(id))
        }
    }

    @Test
    fun toggle() {
        selectionManager.setChecked(0L, true)
        selectionManager.setChecked(1L, true)
        selectionManager.setChecked(2L, true)
        selectionManager.setChecked(3L, true)

        selectionManager.toggle(0L)
        selectionManager.toggle(1L)
        assertFalse(selectionManager.isChecked(0L))
        assertFalse(selectionManager.isChecked(1L))
        assertTrue(selectionManager.isChecked(2L))
        assertTrue(selectionManager.isChecked(3L))
    }

    @Test
    fun getCount() {
        for (id in ids) {
            selectionManager.setChecked(id, true)
        }
        val observer = LoggingObserver<Int>()
        selectionManager.getCount().observeForever(observer)
        assertEquals(observer.value, ids.size)
    }

    @Test
    fun stressTest() {
        val count = 1000000
        val expectMeasuredTime = 1000
        val measuredTime = measureTimeMillis {
            for (i in 0 until count) {
                selectionManager.setChecked(i.toLong(), true)
            }
            for (i in 0 until count) {
                selectionManager.setChecked(i.toLong(), false)
            }
        }
        assertTrue(expectMeasuredTime >= measuredTime) //867ms
    }

    @Test
    fun stressTest2() {
        val application: Application = ApplicationProvider.getApplicationContext()
        val viewModel = PickleViewModel(application)
        val dummyItems = ArrayList<PickleItem>()
        val count = 100

        for (i in 0 until count) {
            val item = PickleItem(Image(i.toLong(), Uri.EMPTY, "", 0, 0, 0, "", 0))
            dummyItems.add(item)
        }

        viewModel.selectionManager.setChecked(0, true)
        viewModel.selectionManager.setChecked(1, true)
        viewModel.selectionManager.setChecked(3, true)

        viewModel.selectionManager.setChecked(count.toLong()-1, true)
        viewModel.selectionManager.setChecked(count.toLong()-2, true)
        viewModel.selectionManager.setChecked(count.toLong(), true)

        val items: LiveData<PagedList<PickleItem>> =
            Mockito.spy(MutableLiveData(mockPagedList(dummyItems)))
        val spyViewModel = Mockito.spy(viewModel)
        Mockito.`when`(spyViewModel.items).thenReturn(items)

        val measuredTime = measureTimeMillis {
            spyViewModel.getPickleResult()
        }

        println(measuredTime)



    }

    fun <T> mockPagedList(list: List<T>): PagedList<T> {
        val pagedList = Mockito.mock(PagedList::class.java) as PagedList<T>
        Mockito.`when`(pagedList[ArgumentMatchers.anyInt()]).then { invocation ->
            val index = invocation.arguments.first() as Int
            list[index]
        }
        Mockito.`when`(pagedList.size).thenReturn(list.size)
        return pagedList
    }
}