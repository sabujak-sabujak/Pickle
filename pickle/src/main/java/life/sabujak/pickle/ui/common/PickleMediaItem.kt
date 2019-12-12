package life.sabujak.pickle.ui.common

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import life.sabujak.pickle.BR
import life.sabujak.pickle.data.entity.PickleMedia

class PickleMediaItem(val pickleMedia:PickleMedia,
                      val selectionManager:SelectionManager,
                      val onPickleEventListener: OnPickleEventListener): BaseObservable(){


    fun toggle(){
        selectionManager.toggle(getId())
        notifyPropertyChanged(BR.checked)
    }

    @Bindable
    fun getChecked():Boolean{
        return selectionManager.isChecked(getId())
    }

    fun getId()=pickleMedia.getId()

}
