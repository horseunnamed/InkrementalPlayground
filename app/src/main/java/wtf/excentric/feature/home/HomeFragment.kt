package wtf.excentric.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import wtf.excentric.R
import wtf.excentric.core.CmdHandler
import wtf.excentric.core.observeStore
import wtf.excentric.core.storeRenderableView
import wtf.excentric.core.storeVM
import wtf.excentric.feature.favorites.FavoritesFragment
import wtf.excentric.feature.search.SearchFragment


class HomeFragment : Fragment() {

    private val store by storeVM<State, Msg, Nothing>(
        init = State(HomeScreen.SEARCH) to null,
        update = ::update,
        cmdHandler = CmdHandler.empty()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return storeRenderableView(store, ::render)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStore(store) { showScreen(it.selected) }
    }

    private fun showScreen(screen: HomeScreen) {
        val tag = screen.itemId.toString()
        when (screen) {
            HomeScreen.SEARCH -> switchFragment(tag) { SearchFragment() }
            HomeScreen.FAVORITES -> switchFragment(tag) { FavoritesFragment() }
        }
    }

    private fun switchFragment(tag: String, provider: () -> Fragment) {
        val taggedFragment = childFragmentManager.findFragmentByTag(tag)
        val displayingFragment = childFragmentManager.findFragmentById(R.id.home_nav_host)

        childFragmentManager.commit {
            if (displayingFragment != null) {
                detach(displayingFragment)
            }
            if (taggedFragment != null) {
                attach(taggedFragment)
            } else {
                add(R.id.home_nav_host, provider(), tag)
            }
        }
    }

}