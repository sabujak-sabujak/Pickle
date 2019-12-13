package life.sabujak.pickle.ui.common

import life.sabujak.pickle.data.entity.PickleMedia

interface OnEventListener {
    fun onItemClick(pickleMedia: PickleMedia)
    fun onItemLongClick(pickleMedia: PickleMedia):Boolean
}
