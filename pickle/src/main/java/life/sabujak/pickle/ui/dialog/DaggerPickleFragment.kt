package life.sabujak.pickle.ui.dialog

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class DaggerPickleFragment : BottomSheetDialogFragment(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    private fun inject() {
        val fragmentInjector = fragmentInjector() as AndroidInjector<DaggerPickleFragment>
        fragmentInjector.inject(this)
    }

    abstract fun fragmentInjector(): AndroidInjector<out DaggerPickleFragment>

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}