package wtf.excentric.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import wtf.excentric.core.observeStore
import wtf.excentric.core.storeRenderableView
import wtf.excentric.core.storeVM


class SearchFragment : Fragment() {

    private val store by storeVM(
        init = State(null, false) to Msg.Load,
        update = ::update,
        cmdHandler = SearchFx()
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
        observeStore(store)
    }

}