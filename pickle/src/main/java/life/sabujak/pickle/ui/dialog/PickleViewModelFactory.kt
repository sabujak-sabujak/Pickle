package life.sabujak.pickle.ui.dialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PickleViewModelFactory(private val application: Application, private val config:Config): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PickleViewModel(application, config) as T
    }

}