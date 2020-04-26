package life.sabujak.picklesample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import life.sabujak.pickle.data.entity.Media
import life.sabujak.picklesample.databinding.ItemViewBinding


class RvAdapter(private val mediaItems: ArrayList<Media>) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_view, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mediaItems[position]
        holder.apply {
            bind(item)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int {
        return mediaItems.size
    }

    class ViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Media) {
            Glide.with(binding.imageView).load(item.uri).into(binding.imageView)
        }
    }
}