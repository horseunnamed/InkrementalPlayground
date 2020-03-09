package wtf.excentric.core

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import dev.inkremental.Inkremental


inline fun <reified VM : ViewModel> Fragment.viewModel(
    crossinline provider: () -> VM
) = viewModels<VM> {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return provider() as T
        }
    }
}

fun <State, Msg, Cmd> Fragment.storeVM(
    init: Pair<State, Msg?>,
    update: Update<State, Msg, Cmd>,
    cmdHandler: CmdHandler<Cmd, Msg>
) = viewModel {
    StoreVM(init, update, cmdHandler)
}

fun <State, Msg> Fragment.storeRenderableView(
    storeVM: StoreVM<State, Msg, *>,
    render: Render<State, Msg>
) : View {
    val view = MyRenderableView(requireContext())
    Inkremental.mount(view) {
        storeVM.stateLiveData.value?.let { render(it, storeVM::dispatch) }
    }
    return view
}

fun <State> Fragment.observeStore(store: StoreVM<State, *, *>, onNewState: (State) -> Unit = {}) {
    store.stateLiveData
        .distinctUntilChanged()
        .observe(viewLifecycleOwner) {
            Inkremental.render()
            onNewState(it)
        }
}
