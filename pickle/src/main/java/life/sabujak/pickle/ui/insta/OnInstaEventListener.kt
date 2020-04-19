package life.sabujak.pickle.ui.insta

import android.view.View
import life.sabujak.pickle.data.entity.PickleItem

interface OnInstaEventListener {
    fun onItemClick(view: View?, item: PickleItem)
//    fun onItemLongClick(pickleMedia: PickleMedia):Boolean
}