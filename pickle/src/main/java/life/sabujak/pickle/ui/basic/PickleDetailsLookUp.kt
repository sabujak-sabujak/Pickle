package life.sabujak.pickle.ui.basic

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import life.sabujak.pickle.util.Logger

class PickleDetailsLookUp(val recyclerView:RecyclerView) : ItemDetailsLookup<Long>(){
    val logger = Logger.getLogger(PickleDetailsLookUp::class.java.simpleName)
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        logger.d("getItemDetails")
        return recyclerView.findChildViewUnder(e.x, e.y)?.let {
            val bindingHolder = recyclerView.getChildViewHolder(it) as PickleDetails
            return bindingHolder.getItemDetails()
        }?:run{ null }
    }

}