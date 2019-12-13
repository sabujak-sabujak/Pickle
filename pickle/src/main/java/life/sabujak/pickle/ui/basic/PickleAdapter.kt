package life.sabujak.pickle.ui.basic

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.BR
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.ui.common.BindingHolder
import life.sabujak.pickle.ui.common.OnEventListener
import life.sabujak.pickle.ui.common.SelectionManager
import life.sabujak.pickle.ui.common.adapter.PickleBindingComponent
import life.sabujak.pickle.util.Logger

class PickleAdapter(val lifecycle:Lifecycle, val selectionManager:SelectionManager, val onEventListener: OnEventListener): PagedListAdapter<PickleMedia, BindingHolder>(diffCallback) {

    val logger = Logger.getLogger(PickleAdapter::class.java.simpleName)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PickleMedia>() {
            override fun areItemsTheSame(oldItem: PickleMedia, newItem: PickleMedia): Boolean {
                return oldItem.getId() == newItem.getId()
            }

            override fun areContentsTheSame(oldItem: PickleMedia, newItem: PickleMedia): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }



    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.getId()?: run {
            logger.i("getItemId : item not found")
            position.toLong()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.view_pickle_media
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        return BindingHolder(parent, viewType, PickleBindingComponent(lifecycle))
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.binding.setVariable(BR.item,getItem(position))
        holder.binding.setVariable(BR.selectionManager, selectionManager)
        holder.binding.setVariable(BR.onEventListener, onEventListener)
        holder.binding.executePendingBindings()
    }
}

