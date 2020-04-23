package life.sabujak.pickle.data

import android.content.Context
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.test.core.app.ApplicationProvider
import life.sabujak.pickle.data.cursor.CursorType
import life.sabujak.pickle.data.entity.PickleItem
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PickleMediaRepositoryTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun fakeCursorFactory(){
        val cursor = FakeCursorFactory().create(context)
        assertNotNull(cursor)
    }

    private class LoggingObserver<T> : Observer<T> {
        var value : T? = null
        override fun onChanged(t: T?) {
            this.value = t
        }
    }
}