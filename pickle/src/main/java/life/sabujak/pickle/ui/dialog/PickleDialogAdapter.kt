package life.sabujak.pickle.ui.dialog

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.BR
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.ui.common.BindingHolder
import life.sabujak.pickle.util.Logger
import javax.inject.Inject

class PickleDialogAdapter @Inject constructor() : PagedListAdapter<PickleItem, BindingHolder>(diffCallback) {

    val logger = Logger.getLogger(this::class.java.simpleName)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PickleItem>() {
            override fun areItemsTheSame(oldItem: PickleItem, newItem: PickleItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PickleItem, newItem: PickleItem): Boolean {
                return oldItem.media.id == newItem.media.id
            }
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.media?.id ?: run {
            logger.w("item not found")
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
        holder.binding.setVariable(BR.item, getItem(position))
        holder.binding.executePendingBindings()
    }

}