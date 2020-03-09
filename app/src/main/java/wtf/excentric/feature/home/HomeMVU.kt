package wtf.excentric.feature.home

import android.widget.LinearLayout
import androidx.annotation.IdRes
import dev.inkremental.dsl.android.Size
import dev.inkremental.dsl.android.size
import dev.inkremental.dsl.android.weight
import dev.inkremental.dsl.android.widget.frameLayout
import dev.inkremental.dsl.android.widget.linearLayout
import dev.inkremental.dsl.google.android.material.bottomnavigation.bottomNavigationView
import wtf.excentric.R
import wtf.excentric.core.Dispatch
import wtf.excentric.core.Next
import wtf.excentric.core.menuRes

enum class HomeScreen(@IdRes val itemId: Int) {
    SEARCH(R.id.home_search_item),
    FAVORITES(R.id.home_favorites_item)
}

data class State(
    val selected: HomeScreen
)

sealed class Msg {
    data class Select(val screen: HomeScreen) : Msg()
}

fun update(state: State, msg: Msg): Next<State, Nothing> = when (msg) {
    is Msg.Select -> state.copy(selected = msg.screen) to emptySet()
}

fun render(state: State, dispatch: Dispatch<Msg>) {
    linearLayout {
        size(Size.MATCH, Size.MATCH)
        orientation(LinearLayout.VERTICAL)

        frameLayout {
            id(R.id.home_nav_host)
            size(Size.MATCH, 0.sizeDp)
            weight(1f)
        }

        bottomNavigationView {
            menuRes(R.menu.home)
            size(Size.MATCH, Size.WRAP)

            selectedItemId(state.selected.itemId)

            onNavigationItemSelected { menuItem ->
                val screen = HomeScreen.values().find { it.itemId == menuItem.itemId }
                if (screen != null && screen != state.selected) {
                    dispatch(Msg.Select(screen))
                    true
                } else {
                    false
                }
            }
        }
    }
}
