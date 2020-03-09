package wtf.excentric.feature.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.inkremental.dsl.android.text
import dev.inkremental.dsl.android.widget.textView
import dev.inkremental.renderable


class FavoritesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return renderable(inflater.context) {
            textView {
                text("Hello, Favs!")
            }
        }
    }
}