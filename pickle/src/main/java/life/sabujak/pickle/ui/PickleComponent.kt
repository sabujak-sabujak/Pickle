package life.sabujak.pickle.ui

import dagger.BindsInstance
import dagger.Component
import life.sabujak.pickle.di.PickleScope

@PickleScope
@Component(modules = [PickleModule::class])
interface PickleComponent{

    fun inject(activity: PickleActivity)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance activity: PickleActivity):PickleComponent
    }


}