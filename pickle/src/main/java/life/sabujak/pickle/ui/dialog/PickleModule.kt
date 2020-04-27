package life.sabujak.pickle.ui.dialog

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.Module
import dagger.Provides
import life.sabujak.pickle.R
import life.sabujak.pickle.dagger.PickleScope
import life.sabujak.pickle.util.recyclerview.GridSpaceDecoration
import life.sabujak.pickle.util.ScreenUtil
import javax.inject.Named

@Module
class PickleModule {

    @Provides
    @PickleScope
    fun provideContext(fragment: PickleDialogFragment) = fragment.requireContext()

    @Provides
    @PickleScope
    @Named("columnCount")
    fun provideColumnCount(context: Context) =
        ScreenUtil.getColumnCount(context, R.dimen.pickle_column_width)

    @Provides
    @PickleScope
    fun provideLayoutManager(
        context: Context,
        @Named("columnCount") columnCount: Int
    ): RecyclerView.LayoutManager =
        GridLayoutManager(
            context,
            columnCount
        )

    @Provides
    @PickleScope
    fun provideGridSpaceDecoration(context: Context, @Named("columnCount") columnCount: Int) =
        GridSpaceDecoration(
            columnCount,
            ScreenUtil.dpToPx(context, 3f),
            false
        )
}
