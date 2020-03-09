package wtf.excentric

import android.app.Application
import dev.inkremental.Inkremental
import wtf.excentric.core.CustomSetters


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Inkremental.registerAttributeSetter(CustomSetters)
    }

}