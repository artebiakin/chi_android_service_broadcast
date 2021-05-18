package co.qweex.chi_android_service_broadcast

import android.annotation.SuppressLint
import android.content.*
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private val CHANNEL = "chi.android.service.broadcast/downloading"
    private var downloadStatusStreamEvent: EventChannel.EventSink? = null

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "startDownload" -> Intent(this, DownloadingService::class.java).also { intent ->
                    startService(intent)
                    result.success(null)
                }
                "stopDownload" -> Intent(this, DownloadingService::class.java).also { intent ->
                    stopService(intent)
                    result.success(null)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }

            val eventChannel = EventChannel(flutterEngine.dartExecutor.binaryMessenger, "downloadStatusStream")

            eventChannel.setStreamHandler(
                    object : EventChannel.StreamHandler {
                        override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                            downloadStatusStreamEvent = events;
                        }

                        override fun onCancel(arguments: Any?) {
                            downloadStatusStreamEvent = null
                        }
                    }
            )
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, IntentFilter(DownloadingService.NOTIFICATION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent) {
            var progressInPercent = intent.getIntExtra(DownloadingService.NUMBER, 0)
            if(downloadStatusStreamEvent != null) {
                downloadStatusStreamEvent?.success(progressInPercent)
            }
        }
    }
}
