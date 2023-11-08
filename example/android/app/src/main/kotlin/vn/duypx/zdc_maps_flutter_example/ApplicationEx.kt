package vn.duypx.zdc_maps_flutter_example

import android.widget.Toast
import io.flutter.app.FlutterApplication
import com.zdc.android.zms.maps.ErrorCode
import com.zdc.android.zms.maps.MapInitializer

class ApplicationEx : FlutterApplication() {

    // TODO need update value
    private val serverDomain = ""
    private val consumerKey = ""
    private val consumerSecret = ""

    override fun onCreate() {
        super.onCreate()

        val ret = MapInitializer.initialize(
            applicationContext,
            serverDomain,
            consumerKey,
            consumerSecret
        )
        if (ret != ErrorCode.ZDCMAP_OK) {
            Toast.makeText(this@ApplicationEx, "initialize error :$ret", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}