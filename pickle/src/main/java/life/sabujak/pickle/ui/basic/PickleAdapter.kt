package life.sabujak.pickle.ui.basic

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import life.sabujak.pickle.BR
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.util.Logger

class PickleAdapter : PagedListAdapter<PickleMedia, PickleDetailsHolder>(diffCallback) {

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

    var selectionTracker:SelectionTracker<Long>? = null

    override fun getItemId(position: Int): Long {
        return getItem(position)?.let {
            it.getId()
        } ?: run {
            logger.i("getItemId : item not found")
            position.toLong()
        }

    }

    override fun getItemViewType(position: Int): Int {
//        logger.i("getItemViewType position = $position type =${getItem(position)}")
        return R.layout.view_pickle_media
//        return when(getItem(position)?.getType()){
//            PickleMedia.Type.PHOTO -> R.layout.view_pickle_media
//            PickleMedia.Type.VIDEO -> R.layout.view_pickle_media
//            else -> R.layout.view_pickle_media_placeholder
////            else->R.layout.view_pickle_media
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickleDetailsHolder {
        return PickleDetailsHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: PickleDetailsHolder, position: Int) {
        val item = getItem(position)
        holder.binding.setVariable(BR.pickleMedia,item)
        holder.binding.setVariable(BR.selectionTracker,selectionTracker)
        holder.binding.executePendingBindings()

    }


}