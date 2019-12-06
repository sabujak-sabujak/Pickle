package life.sabujak.pickle.ui.insta

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.view_insta_media.view.*
import life.sabujak.pickle.BR
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.ui.BindingHolder
import life.sabujak.pickle.util.Logger

class InstaAdapter : PagedListAdapter<PickleMedia, BindingHolder>(diffCallback) {

    val logger = Logger.getLogger("InstaAdapter")

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null
    var lastClickedPosition: Int = -1
    var lastClickedView: View? = null

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
        return R.layout.view_insta_media
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder{
        return BindingHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.setVariable(BR.pickleMedia,item)
            if(position != lastClickedPosition) holder.itemView.v_cover.visibility = View.INVISIBLE
            holder.itemView.setOnClickListener{v->
                logger.d("item Clicked ")
                holder.itemView.v_cover.visibility = View.VISIBLE
                if(lastClickedPosition != -1 && lastClickedPosition != position) unSelectLastItem()
                lastClickedPosition = position
                lastClickedView = holder.itemView
                itemClick?.onClick(v, position)
            }
            holder.binding.executePendingBindings()
        }

    }

    fun unSelectLastItem(){
        lastClickedView?.let {
            it.v_cover.visibility = View.INVISIBLE
        }
    }
    fun getPickleMeida(position: Int): PickleMedia?{
        return getItem(position)
    }
}