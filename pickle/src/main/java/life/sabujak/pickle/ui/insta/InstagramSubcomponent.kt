package life.sabujak.pickle.ui.insta

import com.charlezz.finalarchitecture.di.FragmentScope
import dagger.Subcomponent

@Subcomponent(modules = [InstagramModule::class])
@FragmentScope
interface InstagramSubcomponent{
    @Subcomponent.Factory
    interface Factory{
        fun create():InstagramSubcomponent
    }
}