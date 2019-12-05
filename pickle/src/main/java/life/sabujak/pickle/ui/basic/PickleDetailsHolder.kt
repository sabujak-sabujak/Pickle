package life.sabujak.pickle.ui.basic

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.selection.ItemDetailsLookup
import life.sabujak.pickle.ui.BindingHolder

class PickleDetailsHolder : BindingHolder,
    PickleDetails {

    constructor(binding: ViewDataBinding):super(binding)
    constructor(parent: ViewGroup, layoutResId:Int):super(parent, layoutResId)

    override fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> {
        return object:ItemDetailsLookup.ItemDetails<Long>(){
            override fun getSelectionKey(): Long = itemId
            override fun getPosition(): Int = adapterPosition
        }
    }

}