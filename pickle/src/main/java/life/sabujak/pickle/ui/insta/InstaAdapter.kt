package life.sabujak.pickle.ui.insta

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.ui.BindingHolder

class InstaAdapter : PagedListAdapter<PickleMedia, BindingHolder>(diffCallback) {


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

    override fun getItemViewType(position: Int): Int {
        return getItem(position)
            ?.let { it.getType().layoutResId }
            ?: run { PickleMedia.Type.PHOTO.layoutResId }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder{
        return BindingHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.setVariable(item.getType().variableId,item)
        }

    }

}