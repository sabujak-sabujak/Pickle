package life.sabujak.pickle.ui.insta

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.BR
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.ui.common.BindingHolder
import life.sabujak.pickle.ui.common.adapter.PickleBindingComponent
import life.sabujak.pickle.util.Logger

class InstaAdapter(val lifecycle: Lifecycle, private val selectionManager:InstaSelectionManager, val onEventListener: OnInstaEventListener ) : PagedListAdapter<PickleItem, BindingHolder>(diffCallback) {

    val logger = Logger.getLogger(this.javaClass.simpleName)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PickleItem>() {
            override fun areItemsTheSame(oldItem: PickleItem, newItem: PickleItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PickleItem, newItem: PickleItem): Boolean {
                return oldItem.getId() == newItem.getId()
            }
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.getId() ?: run {
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
}