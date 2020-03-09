package wtf.excentric.core

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class StoreVM<State, Msg, Cmd>(
    init: Pair<State, Msg?>,
    private val update: Update<State, Msg, Cmd>,
    private val cmdHandler: CmdHandler<Cmd, Msg>
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<State>(init.first)
    val stateLiveData: LiveData<State> = _stateLiveData

    init {
        init.second?.let(this::dispatch)
    }

    @MainThread
    fun dispatch(msg: Msg) {
        viewModelScope.launch {
            val (newState, cmdSet) = update(_stateLiveData.value!!, msg)
            _stateLiveData.value = newState
            cmdSet.forEach {
                cmdHandler.accept(it, this@StoreVM::dispatch)
            }
        }
    }

}