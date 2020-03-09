@file:Suppress("unused")

package wtf.excentric.core

import android.view.View
import android.widget.ImageView
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.inkremental.Inkremental
import dev.inkremental.attr
import dev.inkremental.dsl.android.widget.ImageViewScope
import dev.inkremental.dsl.androidx.recyclerview.widget.RecyclerViewScope
import dev.inkremental.dsl.google.android.material.bottomnavigation.BottomNavigationViewScope


data class ListAdapterAttrs<T>(
    val items: List<T>,
    val diff: DiffUtil.ItemCallback<T>,
    val itemRenderer: ItemRenderer<T>
)

fun BottomNavigationViewScope.menuRes(@MenuRes menuId: Int) = attr("inflateMenu", menuId)

fun <T> RecyclerViewScope.listAdapter(
    items: List<T>,
    diff: DiffUtil.ItemCallback<T>,
    itemRenderer: ItemRenderer<T>
) = attr(
    "listAdapter",
    ListAdapterAttrs(items, diff, itemRenderer)
)

fun ImageViewScope.imageUrl(url: String) = attr("imageUrl", url)

object CustomSetters : Inkremental.AttributeSetter<Any> {

    @Suppress("UNCHECKED_CAST")
    override fun set(v: View, name: String, value: Any?, prevValue: Any?): Boolean = when (name) {
        "inflateMenu" -> when {
            v is BottomNavigationView && value is Int -> {
                v.inflateMenu(value)
                true
            }
            else -> false
        }

        "listAdapter" -> when (v) {
            is RecyclerView -> {
                val adapterAttrs = value as ListAdapterAttrs<Any>
                if (v.adapter == null) {
                    v.adapter = MyRecyclerViewAdapter(
                        adapterAttrs.diff,
                        adapterAttrs.itemRenderer
                    )
                }
                (v.adapter as MyRecyclerViewAdapter<Any>).submitList(adapterAttrs.items)
                true
            }
            else -> false
        }

        "imageUrl" -> when {
            v is ImageView && value is String -> {
                v.load(value)
                true
            }
            else -> false
        }

        else -> false
    }

}