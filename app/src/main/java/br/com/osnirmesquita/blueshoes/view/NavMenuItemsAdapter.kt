package br.com.osnirmesquita.blueshoes.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import br.com.osnirmesquita.blueshoes.R
import br.com.osnirmesquita.blueshoes.domain.NavMenuItem
import br.com.osnirmesquita.blueshoes.extensions.gone
import br.com.osnirmesquita.blueshoes.extensions.visible
import br.com.osnirmesquita.blueshoes.util.NavMenuItemDetails
import kotlinx.android.synthetic.main.nav_menu_item.view.*

class NavMenuItemsAdapter(private val items: List<NavMenuItem>) :
    RecyclerView.Adapter<NavMenuItemsAdapter.ViewHolder>() {

    lateinit var selectionTracker: SelectionTracker<Long>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.nav_menu_item, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemDetails: NavMenuItemDetails = NavMenuItemDetails()

        fun setData(item: NavMenuItem) {
            with(itemView) {
                tv_label.text = item.label

                if (item.iconId != NavMenuItem.DEFAULT_ICON_ID) {
                    iv_icon.apply {
                        setImageResource(item.iconId)
                        visible()
                    }
                } else {
                    iv_icon.gone()
                }

                itemDetails.item = item
                itemDetails.adapterPosition = adapterPosition

                if (selectionTracker.isSelected(itemDetails.selectionKey)) {
                    setBackgroundColor(
                        ContextCompat.getColor(context,
                            R.color.colorNavItemSelected
                        )
                    )
                } else {
                    itemView.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }
    }
}