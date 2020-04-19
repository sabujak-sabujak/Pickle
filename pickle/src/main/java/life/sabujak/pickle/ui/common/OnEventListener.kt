package life.sabujak.pickle.ui.common

import life.sabujak.pickle.data.entity.Media

interface OnEventListener {
    fun onItemClick(media: Media)
    fun onItemLongClick(media: Media):Boolean
}
