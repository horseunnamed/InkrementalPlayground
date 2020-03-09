package wtf.excentric.core

import android.content.Context
import android.widget.FrameLayout
import dev.inkremental.Inkremental

class MyRenderableView(context: Context) : FrameLayout(context) {

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Inkremental.unmount(this, true)
    }

}