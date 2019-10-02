package br.com.osnirmesquita.blueshoes.view

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.osnirmesquita.blueshoes.R
import br.com.osnirmesquita.blueshoes.data.NavMenuItemsDataBase
import br.com.osnirmesquita.blueshoes.domain.NavMenuItem
import br.com.osnirmesquita.blueshoes.domain.User
import br.com.osnirmesquita.blueshoes.extensions.gone
import br.com.osnirmesquita.blueshoes.util.NavMenuItemDetailsLookup
import br.com.osnirmesquita.blueshoes.util.NavMenuItemKeyProvider
import br.com.osnirmesquita.blueshoes.util.NavMenuItemPredicate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_user_logged.*
import kotlinx.android.synthetic.main.nav_header_user_not_logged.*
import kotlinx.android.synthetic.main.nav_menu.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val FRAGMENT_TAG = "frag-tag"
    }


    lateinit var navMenuItems: List<NavMenuItem>
    lateinit var selectNavMenuItems: SelectionTracker<Long>
    lateinit var navMenuItemsLogged: List<NavMenuItem>
    lateinit var selectNavMenuItemsLogged: SelectionTracker<Long>
    private val user = User("Osnir Mesquita", R.drawable.user, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        initNavMenu(savedInstanceState)

        initFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        selectNavMenuItems.onSaveInstanceState(outState)
        selectNavMenuItemsLogged.onSaveInstanceState(outState)
    }

    private fun fillUserHeaderNavMenu() {
        user.takeIf { it.status }
            ?.apply {
                iv_user.setImageResource(image)
                tv_user.text = name
            }
    }

    private fun showHideNavMenuViews() {
        if (user.status) {
            rl_header_user_not_logged.gone()
            fillUserHeaderNavMenu()
        } else {
            rl_header_user_logged.gone()
            v_nav_vertical_line.gone()
            rv_menu_items_logged.gone()
        }
    }

    private fun initNavMenu(savedInstanceState: Bundle?) {
        with(NavMenuItemsDataBase(this)) {
            navMenuItems = items
            navMenuItemsLogged = itemsLogged
        }

        showHideNavMenuViews()

        initNavMenuItems()
        initNavMenuItemsLogged()

        if (savedInstanceState != null) {
            selectNavMenuItems.onRestoreInstanceState(savedInstanceState)
            selectNavMenuItemsLogged.onRestoreInstanceState(savedInstanceState)
        } else {
            selectNavMenuItems.select(R.id.item_all_shoes.toLong())
        }
    }

    private fun initNavMenuItems() {
        rv_menu_items.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = NavMenuItemsAdapter(navMenuItems)
        }

        initNaeMenuItemsSelection()
    }

    private fun initNaeMenuItemsSelection() {
        selectNavMenuItems = SelectionTracker.Builder<Long>(
            "id-selected-item",
            rv_menu_items,
            NavMenuItemKeyProvider(navMenuItems),
            NavMenuItemDetailsLookup(rv_menu_items),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            NavMenuItemPredicate(this)
        ).build()

        (rv_menu_items.adapter as NavMenuItemsAdapter).selectionTracker = selectNavMenuItems

        selectNavMenuItems.addObserver(SelectObserverNavMenuItems {
            selectNavMenuItemsLogged.selection.filter { selectNavMenuItemsLogged.deselect(it) }
        })
    }

    private fun initNavMenuItemsLogged() {
        rv_menu_items_logged.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = NavMenuItemsAdapter(navMenuItemsLogged)
        }

        initNavMenuItemsLoggedSelection()
    }

    private fun initNavMenuItemsLoggedSelection() {
        selectNavMenuItemsLogged = SelectionTracker.Builder<Long>(
            "id-selected-item-logged",
            rv_menu_items_logged,
            NavMenuItemKeyProvider(navMenuItemsLogged),
            NavMenuItemDetailsLookup(rv_menu_items_logged),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            NavMenuItemPredicate(this)
        ).build()

        (rv_menu_items_logged.adapter as NavMenuItemsAdapter).selectionTracker =
            selectNavMenuItemsLogged

        selectNavMenuItemsLogged.addObserver(SelectObserverNavMenuItems {
            val selection = selectNavMenuItems.selection
            selection.filter { selectNavMenuItems.deselect(it) }
        })
    }

    private fun initFragment() {
        val supFrag = supportFragmentManager
        var fragment = supFrag.findFragmentByTag(FRAGMENT_TAG)

        if (fragment == null) {
            fragment = getFragment(R.id.item_about.toLong())
        }

        replaceFragment(fragment)
    }

    private fun getFragment(fragmentId: Long) = when (fragmentId) {
        R.id.item_about.toLong() -> AboutFragment()
        else -> AboutFragment()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_fragment_container, fragment, FRAGMENT_TAG)
            .commit()

    }

    fun updateToolbarTitleInFragment(titleStringId: Int) {
        toolbar.title = getString(titleStringId)
    }

    inner class SelectObserverNavMenuItems(
        private val callbackRemoveSelection: () -> Unit
    ) : SelectionTracker.SelectionObserver<Long>() {
        override fun onItemStateChanged(key: Long, selected: Boolean) {
            super.onItemStateChanged(key, selected)

            if (!selected) {
                return
            }

            callbackRemoveSelection.invoke()

            val fragment = getFragment(key)
            replaceFragment(fragment)

            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }
}
