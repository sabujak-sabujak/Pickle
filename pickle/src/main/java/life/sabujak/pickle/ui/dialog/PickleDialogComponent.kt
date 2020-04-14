package life.sabujak.pickle.ui.dialog

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import life.sabujak.pickle.dagger.PickleScope

@Component(modules = [PickleModule::class, AndroidSupportInjectionModule::class])
@PickleScope
interface PickleDialogComponent : AndroidInjector<PickleDialogFragment> {
    @Component.Factory
    interface Factory : AndroidInjector.Factory<PickleDialogFragment>
}