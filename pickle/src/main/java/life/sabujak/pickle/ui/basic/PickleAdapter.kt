package life.sabujak.pickle.ui.basic

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.BR
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.ui.BindingHolder
import life.sabujak.pickle.util.Logger

class PickleAdapter : PagedListAdapter<PickleMedia, BindingHolder>(diffCallback) {

    val logger = Logger.getLogger(PickleAdapter::class.java.simpleName)

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PickleMedia>() {
            override fun areItemsTheSame(oldItem: PickleMedia, newItem: PickleMedia): Boolean {
                return oldItem.getId() == newItem.getId()
            }

            override fun areContentsTheSame(oldItem: PickleMedia, newItem: PickleMedia): Boolean {
                return false
            }
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.let {
            it.getId()
        } ?: run {
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
        val item = getItem(position)
        holder.binding.setVariable(BR.pickleMedia,item)
        holder.binding.executePendingBindings()

    }


}