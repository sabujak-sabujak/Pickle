package life.sabujak.pickle.ui.common.adapter

import androidx.databinding.DataBindingComponent
import androidx.lifecycle.Lifecycle

class PickleBindingComponent(lifecycle:Lifecycle): DataBindingComponent{
    @JvmField val clickAdapter =ClickAdapter(lifecycle)

    override fun getClickAdapter(): ClickAdapter {
        return clickAdapter
    }
}