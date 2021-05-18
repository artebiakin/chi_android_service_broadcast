package com.chi.chi_android_service_broadcast

import com.chi.chi_android_service_broadcast.DownloadService
import android.content.*
import androidx.annotation.NonNull
import androidx.core.app.JobIntentService.enqueueWork
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.chi.chi_android_service_broadcast.models.Download
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private val CHANNEL = "chi.android.service.broadcast/downloading"
    private var downloadStatusStreamEvent: EventChannel.EventSink? = null

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        registerReceiver()

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "startDownload" -> Intent(this, DownloadService::class.java).also { intent ->
                    enqueueWork(this, DownloadService::class.java, 1, intent)
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
                        downloadStatusStreamEvent?.success(0)
                        downloadStatusStreamEvent = events;
                    }

                    override fun onCancel(arguments: Any?) {
                        downloadStatusStreamEvent = null
                    }
                }
        )
    }

    private fun registerReceiver() {
        val bManager = LocalBroadcastManager.getInstance(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(DownloadService.NOTIFICATION)
        bManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == DownloadService.NOTIFICATION) {
                val download: Download = intent.getParcelableExtra(DownloadService.RESULT)!!

                if (download.progress == 100) {
                    downloadStatusStreamEvent?.success(100)
                } else {
                    downloadStatusStreamEvent?.success(download.progress)
                }
            }
        }
    }
}
