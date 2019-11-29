package life.sabujak.pickle.ui

import androidx.databinding.DataBindingUtil
import dagger.Module
import dagger.Provides
import life.sabujak.pickle.R
import life.sabujak.pickle.databinding.ActivityPickleBinding
import life.sabujak.pickle.di.PickleScope
import life.sabujak.pickle.di.ViewModelModule
import life.sabujak.pickle.ui.insta.InstagramSubcomponent

@Module(includes = [ViewModelModule::class], subcomponents = [InstagramSubcomponent::class])
class PickleModule{

    @Provides
    @PickleScope
    fun provideBinding(activity:PickleActivity): ActivityPickleBinding {
        return DataBindingUtil.setContentView(activity, R.layout.activity_pickle)
    }



}