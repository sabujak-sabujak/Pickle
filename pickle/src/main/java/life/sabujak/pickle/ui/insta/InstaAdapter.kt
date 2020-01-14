package life.sabujak.pickle.ui.insta

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.BR
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.ui.common.BindingHolder
import life.sabujak.pickle.ui.common.adapter.PickleBindingComponent
import life.sabujak.pickle.util.Logger

class InstaAdapter(val lifecycle: Lifecycle, val selectionManager:InstaSelectionManager, val onEventListener: OnInstaEventListener ) : PagedListAdapter<PickleMedia, BindingHolder>(diffCallback) {

    val logger = Logger.getLogger(this.javaClass.simpleName)

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
        return R.layout.view_insta_media
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        return BindingHolder(parent, viewType, PickleBindingComponent(lifecycle))
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.setVariable(BR.item, it)
            holder.binding.setVariable(BR.instaSelectionManager, selectionManager)
            holder.binding.setVariable(BR.onEventListener, onEventListener)
            holder.binding.executePendingBindings()
        }
    }

    override fun submitList(pagedList: PagedList<PickleMedia>?) {
        super.submitList(pagedList)
        if(pagedList?.size != 0) {
            onEventListener.onItemClick(null, pagedList?.get(0)!!)
        }
    }
}