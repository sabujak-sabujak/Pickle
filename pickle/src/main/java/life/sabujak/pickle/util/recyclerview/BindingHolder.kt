package life.sabujak.pickle.util.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BindingHolder<VDB : ViewDataBinding>: RecyclerView.ViewHolder{
    val binding : VDB

    constructor(binding: VDB):super(binding.root){
        this.binding = binding
    }
    constructor(parent:ViewGroup,layoutResId:Int):this(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false)
    )
    constructor(parent:ViewGroup,layoutResId:Int, bindingComponent: DataBindingComponent):this(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false, bindingComponent)
    )

    abstract fun bind(holder : BindingHolder<VDB>, position : Int)

}

