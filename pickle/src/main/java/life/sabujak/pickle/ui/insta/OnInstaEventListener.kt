package life.sabujak.pickle.ui.insta

import android.view.View
import life.sabujak.pickle.data.entity.PickleMedia

interface OnInstaEventListener {
    fun onItemClick(view: View?, pickleMedia: PickleMedia)
//    fun onItemLongClick(pickleMedia: PickleMedia):Boolean
}