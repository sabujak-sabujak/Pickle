package life.sabujak.pickle.ui.dialog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import life.sabujak.pickle.LoggingObserver
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class SelectionManagerTest {
    @Suppress("unused")
    @get:Rule // LiveData가 동기적으로 작업을 하도록 함
    val instantExecutor = InstantTaskExecutorRule()
    lateinit var selectionManager: SelectionManager
    private val ids = arrayOf(0L,1L,2L,3L)
    @Before
    fun setup(){
        selectionManager = SelectionManager()
    }

    @Test
    fun setChecked() {
        for (id in ids){
            selectionManager.setChecked(id, true)
        }
        for( id in ids){
            assertTrue(selectionManager.selectedIds.contains(id))
        }
        for(id in ids){
            selectionManager.setChecked(id, false)
        }
        for( id in ids){
            assertFalse(selectionManager.selectedIds.contains(id))
        }
    }

    @Test
    fun isChecked() {
        for (id in ids){
            selectionManager.setChecked(id, true)
        }
        for( id in ids){
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
        for (id in ids){
            selectionManager.setChecked(id, true)
        }
        val observer = LoggingObserver<Int>()
        selectionManager.getCount().observeForever(observer)
        assertEquals(observer.value, ids.size)
    }
}