package life.sabujak.pickle.ui.insta

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.BR
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.databinding.ViewInstaMediaBinding
import life.sabujak.pickle.util.recyclerview.BindingHolder
import life.sabujak.pickle.util.bindingadapter.PickleBindingComponent
import life.sabujak.pickle.util.Logger

class InstaAdapter(val lifecycle: Lifecycle, val selectionManager:InstaSelectionManager, val onEventListener: OnInstaEventListener )
    : PagedListAdapter<PickleItem, BindingHolder<ViewInstaMediaBinding>>(diffCallback) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ViewInstaMediaBinding> {
        return object:
            BindingHolder<ViewInstaMediaBinding>(parent, viewType,
            PickleBindingComponent(
                lifecycle
            )
        ){
            override fun bind(holder: BindingHolder<ViewInstaMediaBinding>, position: Int) {
                val item = getItem(position)
                item?.let {
                    holder.binding.setVariable(BR.item, it)
                    holder.binding.setVariable(BR.instaSelectionManager, selectionManager)
                    holder.binding.setVariable(BR.onEventListener, onEventListener)
                    holder.binding.executePendingBindings()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BindingHolder<ViewInstaMediaBinding>, position: Int) {
        holder.bind(holder, position)

    }
}