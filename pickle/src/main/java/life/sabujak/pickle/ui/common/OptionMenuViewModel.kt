package life.sabujak.pickle.ui.common

import android.app.Application
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import life.sabujak.pickle.R
import life.sabujak.pickle.databinding.ViewTextOptionsMenuBinding
import life.sabujak.pickle.ui.common.adapter.PickleBindingComponent
import life.sabujak.pickle.util.SingleLiveEvent

class OptionMenuViewModel(val app: Application) : AndroidViewModel(app),
    LifecycleOwner {

    private val pickleBindingComponent: PickleBindingComponent
    private val binding: ViewTextOptionsMenuBinding
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this).apply {
        handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    val title = MutableLiveData(app.getString(R.string.select))
    val isEnabled = MutableLiveData(true)
    val count = MutableLiveData(0)
    val isCountVisible = MutableLiveData(true)
    val textColor = Transformations.map(isEnabled) {
        if (it) {
            ContextCompat.getColor(app, R.color.TC02)
        } else {
            ContextCompat.getColor(app, R.color.TC01)
        }
    }

    val clickEvent = SingleLiveEvent<Void>()

    init {
        pickleBindingComponent = PickleBindingComponent(lifecycleRegistry)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(app),
            R.layout.view_text_options_menu,
            null,
            false,
            pickleBindingComponent
        )
    }

    fun onCreateOptionMenu(menu: Menu?): MenuItem? {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        binding.lifecycleOwner = this
        binding.viewModel = this
        val menuItem = menu?.add(R.string.confirm)
        menuItem?.actionView = binding.root
        menuItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return menuItem
    }

    fun onClick() {
        if (isEnabled.value!!) {
            clickEvent.call()
        }
    }

    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    fun setCountable(isCountable: Boolean){
        isCountVisible.value = isCountable
    }
}