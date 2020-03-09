package wtf.excentric.core

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.inkremental.Inkremental
import dev.inkremental.Inkremental.render
import dev.inkremental.dsl.android.Size
import dev.inkremental.dsl.android.size
import dev.inkremental.dsl.androidx.recyclerview.linearLayoutManager
import dev.inkremental.dsl.androidx.recyclerview.widget.recyclerView
import wtf.excentric.R

typealias ItemRenderer<T> = (Int, T) -> Unit

class MyRecyclerViewAdapter<T>(
    diff: DiffUtil.ItemCallback<T>,
    private val itemRenderer: (Int, T) -> Unit
) : ListAdapter<T, MyRecyclerViewAdapter.MountHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MountHolder {
        val root = FrameLayout(parent.context)
        root.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        return MountHolder(root)
    }

    override fun onBindViewHolder(h: MountHolder, position: Int) {
        if (h.mount == null) {
            h.mount = Inkremental.Mount(h.itemView as ViewGroup) { view(h) }
            render(h.mount!!)
        } else {
            render(h.mount!!)
        }
    }


    override fun getItemId(pos: Int): Long {
        return pos.toLong() // just a most common implementation
    }

    private fun view(holder: RecyclerView.ViewHolder?) {
        holder?.let {
            val i = it.layoutPosition
            itemRenderer(i, getItem(i))
        }
    }

    class MountHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        var mount: Inkremental.Mount? = null
    }

}

class EmptyDiff<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return false
    }
}

fun <T> vList(
    items: List<T>,
    diff: DiffUtil.ItemCallback<T>,
    itemRenderer: ItemRenderer<T>
) {
    recyclerView {
        id(R.id.recycler_view)
        size(Size.MATCH, Size.MATCH)
        linearLayoutManager(orientation = RecyclerView.VERTICAL)
        listAdapter(items, diff, itemRenderer)
    }
}
