package wtf.excentric.feature.search

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.awaitResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import wtf.excentric.core.CmdHandler
import wtf.excentric.core.Dispatch
import wtf.excentric.core.Try


class SearchFx : CmdHandler<Cmd, Msg> {

    override suspend fun accept(cmd: Cmd, dispatch: Dispatch<Msg>) {
        when (cmd) {
            Cmd.Load -> {
                withContext(Dispatchers.IO) {
                    val result = Fuel.get("https://api.thecatapi.com/v1/images/search?limit=10")
                        .awaitResponseResult(object : Deserializable<List<CatImage>> {
                            override fun deserialize(response: Response): List<CatImage> {
                                val json = Json(JsonConfiguration.Default.copy(ignoreUnknownKeys = true))
                                return json.parse(CatImage.serializer().list, response.data.toString(Charsets.UTF_8))
                            }
                        })
                    result.third.fold(
                        { dispatch(Msg.Loaded(Try.Ok(it))) },
                        {
                            Log.e("LoadCmd", it.message, it)
                            dispatch(Msg.Loaded(Try.Nope(it)))
                        }
                    )
                }
            }
        }
    }

}