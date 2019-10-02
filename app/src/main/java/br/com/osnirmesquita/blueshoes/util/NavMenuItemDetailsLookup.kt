package br.com.osnirmesquita.blueshoes.util

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import br.com.osnirmesquita.blueshoes.view.NavMenuItemsAdapter

class NavMenuItemDetailsLookup(private val rvMenuItems: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = rvMenuItems.findChildViewUnder(e.x, e.y)

        if (view != null) {
            val holder = rvMenuItems.getChildViewHolder(view)
            return (holder as NavMenuItemsAdapter.ViewHolder).itemDetails
        }

        return null
    }
}