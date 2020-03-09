package wtf.excentric.feature.search

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.swipeRefreshLayout
import dev.inkremental.dsl.android.*
import dev.inkremental.dsl.android.widget.frameLayout
import dev.inkremental.dsl.android.widget.imageView
import dev.inkremental.dsl.android.widget.progressBar
import dev.inkremental.dsl.android.widget.textView
import dev.inkremental.dsl.google.android.material.card.materialCardView
import dev.inkremental.skip
import kotlinx.serialization.Serializable
import wtf.excentric.R
import wtf.excentric.core.*

// region Model

@Serializable
data class CatImage(
    val id: String,
    val url: String
)

data class State(
    val result: Try<List<CatImage>>?,
    val isInRefresh: Boolean
)

sealed class Msg {
    object Load : Msg()
    object Refresh : Msg()
    class Loaded(val result: Try<List<CatImage>>) : Msg()
}

sealed class Cmd {
    object Load : Cmd()
}

// endregion

// region Update

fun update(state: State, msg: Msg): Next<State, Cmd> = when (msg) {
    Msg.Load -> state.copy(result = null) to setOf(Cmd.Load)
    is Msg.Loaded -> state.copy(result = msg.result, isInRefresh = false) to emptySet()
    Msg.Refresh -> state.copy(isInRefresh = true) to setOf(Cmd.Load)
}

// endregion

// region View

fun render(state: State, dispatch: Dispatch<Msg>) {

    renderLCEStub(state.result) { items ->
        swipeRefreshLayout {
            id(R.id.swipe_refresh_layout)
            size(Size.MATCH, Size.MATCH)
            onRefresh { dispatch(Msg.Refresh) }
            refreshing(state.isInRefresh)

            skip()

            vList(items, EmptyDiff()) { index, item ->
                materialCardView {
                    size(Size.MATCH, 160.sizeDp)
                    when (index) {
                        0 -> margin(l = 16.dp, r = 16.dp, t = 16.dp, b = 8.dp)
                        items.size - 1 -> margin(l = 16.dp, r = 16.dp, t = 8.dp, b = 16.dp)
                        else -> margin(l = 16.dp, r = 16.dp, t = 8.dp, b = 8.dp)
                    }
                    imageView {
                        size(Size.MATCH, Size.MATCH)
                        scaleType(ImageView.ScaleType.CENTER_CROP)
                        imageUrl(item.url)
                    }
                }
            }
        }
    }
}

inline fun <T> renderLCEStub(lce: Try<T>?, crossinline renderContent: (T) -> Unit) {
    when (lce) {
        is Try.Ok -> {
            renderContent(lce.value)
        }

        is Try.Nope -> {
            textView {
                text(lce.error.message ?: "Unknown error")
            }
        }

        null -> {
            frameLayout {
                size(Size.MATCH, Size.MATCH)
                progressBar {
                    layoutGravity(CENTER)
                    size(56.sizeDp, 56.sizeDp)
                }
            }
        }
    }
}

// endregion