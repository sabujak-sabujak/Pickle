package life.sabujak.pickle.ui.dialog

import android.app.Application
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
import life.sabujak.pickle.data.entity.ErrorCode
import life.sabujak.pickle.data.entity.PickleError
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

@RunWith(RobolectricTestRunner::class)
class SelectionManagerTest {
    @Suppress("unused")
    @get:Rule // LiveData가 동기적으로 작업을 하도록 함
    val instantExecutor = InstantTaskExecutorRule()
    private lateinit var selectionManager: SelectionManager
    private val dummyMediaList = ArrayList<Media>()
    private val dummyCount = 5

    private val MAX_MEDIA_COUNT = 1000000//백만

    @Before
    fun setup() {
        val config = Config.Builder(null).build()
        this.selectionManager = SelectionManager(config)

        for (i in 0 until dummyCount) {
            val mockedMedia = Mockito.mock(Media::class.java)
            Mockito.`when`(mockedMedia.id).thenReturn(i.toLong())
            dummyMediaList.add(mockedMedia)
        }
    }

    @Test
    fun setChecked() {
        for (media in dummyMediaList) {
            selectionManager.setChecked(media, true)
        }
        for (media in dummyMediaList) {
            assertTrue(selectionManager.selectedMediaMap.contains(media.id))
        }
        for (media in dummyMediaList) {
            selectionManager.setChecked(media, false)
        }
        for (media in dummyMediaList) {
            assertFalse(selectionManager.selectedMediaMap.contains(media.id))
        }
    }

    @Test
    fun isChecked() {
        for (media in dummyMediaList) {
            selectionManager.setChecked(media, true)
        }
        for (media in dummyMediaList) {
            assertTrue(selectionManager.isChecked(media.id))
        }
    }

    @Test
    fun toggle() {

        for (media in dummyMediaList){
            selectionManager.setChecked(media, true)
        }

        selectionManager.toggle(dummyMediaList[0])
        selectionManager.toggle(dummyMediaList[1])
        assertFalse(selectionManager.isChecked(dummyMediaList[0].id))
        assertFalse(selectionManager.isChecked(dummyMediaList[1].id))
        assertTrue(selectionManager.isChecked(dummyMediaList[2].id))
        assertTrue(selectionManager.isChecked(dummyMediaList[3].id))
        assertTrue(selectionManager.isChecked(dummyMediaList[4].id))
    }

    @Test
    fun getCount() {
        for (media in dummyMediaList) {
            selectionManager.setChecked(media, true)
        }
        val observer = LoggingObserver<Int>()
        selectionManager.getCount().observeForever(observer)
        assertEquals(observer.value, dummyMediaList.size)
    }

    @Test
    fun stressTest() {
        val application: Application = ApplicationProvider.getApplicationContext()
        val config = Config.Builder(null).build()
        val viewModel = PickleViewModel(application, config)
        val dummyItems = ArrayList<PickleItem>()
        val count = MAX_MEDIA_COUNT

        for (i in 0 until count) {
            val item = PickleItem(Image(i.toLong(), Uri.EMPTY, "", 0, 0, 0, "", 0))
            dummyItems.add(item)
        }

        var mockedMedia = Mockito.mock(Media::class.java)
        Mockito.`when`(mockedMedia.id).thenReturn(0)
        viewModel.selectionManager.setChecked(mockedMedia, true)


        mockedMedia = Mockito.mock(Media::class.java)
        Mockito.`when`(mockedMedia.id).thenReturn(1)
        viewModel.selectionManager.setChecked(mockedMedia, true)

        mockedMedia = Mockito.mock(Media::class.java)
        Mockito.`when`(mockedMedia.id).thenReturn(2)
        viewModel.selectionManager.setChecked(mockedMedia, true)

        mockedMedia = Mockito.mock(Media::class.java)
        Mockito.`when`(mockedMedia.id).thenReturn(count.toLong()-1)
        viewModel.selectionManager.setChecked(mockedMedia, true)

        mockedMedia = Mockito.mock(Media::class.java)
        Mockito.`when`(mockedMedia.id).thenReturn(count.toLong()-2)
        viewModel.selectionManager.setChecked(mockedMedia, true)

        mockedMedia = Mockito.mock(Media::class.java)
        Mockito.`when`(mockedMedia.id).thenReturn(count.toLong()-3)
        viewModel.selectionManager.setChecked(mockedMedia, true)

        val items: LiveData<PagedList<PickleItem>> =
            Mockito.spy(MutableLiveData(mockPagedList(dummyItems)))

        val spyViewModel = Mockito.spy(viewModel)
        Mockito.`when`(spyViewModel.items).thenReturn(items)

        val measuredTime = measureTimeMillis {
            spyViewModel.selectionManager.getPickleResult()
        }
        println(measuredTime)
    }

    @Test
    fun maxCount1Test(){
        var errorMsg = ErrorCode.UNKNOWN
        val callback:OnResultListener = object:OnResultListener{
            override fun onSuccess(result: PickleResult) {
            }

            override fun onError(pickleError: PickleError) {
                errorMsg = pickleError.errorCode
            }

        }
        assertEquals(errorMsg, ErrorCode.UNKNOWN)
        val maxCount = 1
        val config = Config.Builder(callback).setMaxSelectionCount(maxCount).build()
        val selectionManager = SelectionManager(config)

        selectionManager.setChecked(mockedMedia(1),true)
        selectionManager.setChecked(mockedMedia(2),true)

        assertEquals(errorMsg, ErrorCode.OVER_MAX_COUNT)

    }

    private fun mockedMedia(id:Long): Media{
        val mockedMedia:Media = Mockito.mock(Media::class.java)
        Mockito.`when`(mockedMedia.id).thenReturn(id)
        return mockedMedia
    }

    private fun <T> mockPagedList(list: List<T>): PagedList<T> {
        val pagedList = Mockito.mock(PagedList::class.java) as PagedList<T>
        Mockito.`when`(pagedList[ArgumentMatchers.anyInt()]).then { invocation ->
            val index = invocation.arguments.first() as Int
            list[index]
        }
        Mockito.`when`(pagedList.size).thenReturn(list.size)
        return pagedList
    }
}