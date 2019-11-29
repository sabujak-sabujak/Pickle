package life.sabujak.pickle.ui

import dagger.BindsInstance
import dagger.Component
import life.sabujak.pickle.di.PickleScope
import life.sabujak.pickle.ui.insta.InstagramSubcomponent

@PickleScope
@Component(modules = [PickleModule::class])
interface PickleComponent{

    fun instagramSubcomponent(): InstagramSubcomponent.Factory

    fun inject(activity: PickleActivity)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance activity: PickleActivity):PickleComponent
    }


}