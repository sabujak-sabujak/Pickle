package life.sabujak.pickle.ui.basic

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.BR
import life.sabujak.pickle.R
import life.sabujak.pickle.ui.common.BindingHolder
import life.sabujak.pickle.ui.common.PickleMediaItem
import life.sabujak.pickle.util.Logger

class PickleAdapter: PagedListAdapter<PickleMediaItem, BindingHolder>(diffCallback) {

    val logger = Logger.getLogger(PickleAdapter::class.java.simpleName)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PickleMediaItem>() {
            override fun areItemsTheSame(oldItem: PickleMediaItem, newItem: PickleMediaItem): Boolean {
                return oldItem.getId() == newItem.getId()
            }

            override fun areContentsTheSame(oldItem: PickleMediaItem, newItem: PickleMediaItem): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.pickleMedia?.getId()?: run {
            logger.i("getItemId : item not found")
            position.toLong()
        }

    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.view_pickle_media
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        return BindingHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.binding.setVariable(BR.item,getItem(position))
        holder.binding.executePendingBindings()
    }
}

