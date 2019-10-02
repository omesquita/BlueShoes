package br.com.osnirmesquita.blueshoes.util

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import br.com.osnirmesquita.blueshoes.domain.NavMenuItem

class NavMenuItemDetails(
    var item: NavMenuItem? = null,
    var adapterPosition: Int = -1
): ItemDetailsLookup.ItemDetails<Long>() {


    override fun getPosition() = adapterPosition

    override fun getSelectionKey() = item!!.id

    override fun inSelectionHotspot(e: MotionEvent) = true
}