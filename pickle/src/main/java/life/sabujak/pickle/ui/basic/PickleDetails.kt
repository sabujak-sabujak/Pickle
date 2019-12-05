package life.sabujak.pickle.ui.basic

import androidx.recyclerview.selection.ItemDetailsLookup

interface PickleDetails{
    fun getItemDetails() : ItemDetailsLookup.ItemDetails<Long>
}