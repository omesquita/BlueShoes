package br.com.osnirmesquita.blueshoes.util

import androidx.recyclerview.selection.ItemKeyProvider
import br.com.osnirmesquita.blueshoes.domain.NavMenuItem

class NavMenuItemKeyProvider(private val items: List<NavMenuItem>) :
    ItemKeyProvider<Long>(SCOPE_MAPPED) {

    override fun getKey(position: Int) = items[position].id

    override fun getPosition(key: Long) =
        items.indexOf(items.first { item -> item.id == key })
}