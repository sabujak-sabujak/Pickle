package life.sabujak.pickle.ui.dialog

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.databinding.ViewPickleMediaBinding
import life.sabujak.pickle.util.recyclerview.BindingHolder
import life.sabujak.pickle.util.Logger
import javax.inject.Inject

class PickleDialogAdapter @Inject constructor() :
    PagedListAdapter<PickleItem, BindingHolder<ViewPickleMediaBinding>>(diffCallback) {

    var selectionManager: SelectionManager? = null
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ViewPickleMediaBinding> {
        return object : BindingHolder<ViewPickleMediaBinding>(parent, viewType) {

            override fun bind(holder: BindingHolder<ViewPickleMediaBinding>, position: Int) {
                val item = getItem(position)

                item?.let { item ->
                    holder.binding.item = item
                    holder.binding.selectionManager = selectionManager
                    holder.binding.executePendingBindings()
                }

            }
        }
    }

    override fun onBindViewHolder(holder: BindingHolder<ViewPickleMediaBinding>, position: Int) {
        holder.bind(holder, position)
    }


}