package life.sabujak.pickle.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BindingHolder: RecyclerView.ViewHolder{
    val binding : ViewDataBinding

    constructor(binding:ViewDataBinding):super(binding.root){
        this.binding = binding
    }

    constructor(parent:ViewGroup,layoutResId:Int):this(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false)
    )


}

