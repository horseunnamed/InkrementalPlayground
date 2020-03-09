package wtf.excentric.core


typealias Next<State, Cmd> = Pair<State, Set<Cmd>>
typealias Update<State, Msg, Cmd> = (State, Msg) -> Next<State, Cmd>
typealias Dispatch<M> = (M) -> Unit
typealias Render<State, Msg> = (State, Dispatch<Msg>) -> Unit

interface CmdHandler<Cmd, Msg> {

    suspend fun accept(cmd: Cmd, dispatch: Dispatch<Msg>)

    companion object {

        inline fun <Cmd, Msg> create(
            crossinline handle: suspend (Cmd, Dispatch<Msg>) -> Unit
        ) : CmdHandler<Cmd, Msg> {
            return object : CmdHandler<Cmd, Msg> {
                override suspend fun accept(cmd: Cmd, dispatch: Dispatch<Msg>) {
                    handle(cmd, dispatch)
                }
            }
        }

        fun <Cmd, Msg> empty() = create<Cmd, Msg> { _, _ -> Unit }

    }

}


