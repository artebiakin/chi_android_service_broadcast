package co.qweex.chi_android_service_broadcast

import android.app.DownloadManager
import android.app.Service
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class DownloadingService : Service() {

    companion object {
        const val NUMBER = "com.chi.chi_android_service_broadcast.result"
        const val NOTIFICATION = "com.chi.chi_android_service_broadcast.notification"
    }

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private var fileName: String = "Video"
    private lateinit var downloadManager: DownloadManager
    private var downloadId: Long = 0

    private var progressInPercent: Int = 0;

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadFile()

        // Do a periodic task
        mHandler = Handler(Looper.getMainLooper())
        mRunnable = Runnable { sendLoadingPercentage(intent) }
        mHandler.postDelayed(mRunnable, 100)
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        downloadManager.remove(downloadId)
        mHandler.removeCallbacks(mRunnable)
    }

    override fun sendBroadcast(intent: Intent?) {
        val intent = Intent(NOTIFICATION)
        intent.putExtra(NUMBER, progressInPercent)
        super.sendBroadcast(intent)
    }

    // Calc progress and sent to Broadcast use sendBroadcast
    private fun sendLoadingPercentage(intent: Intent?) {
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        val cursor: Cursor = downloadManager.query(query)

        if (!cursor.moveToFirst()) {
            onDestroy()
            cursor.close()
            return
        }

        cursor.moveToFirst()
        val bytesDownloaded: Int = cursor.getInt(
                cursor
                        .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
        )

        val bytesTotal: Int =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) === DownloadManager.STATUS_SUCCESSFUL) {
            onDestroy()
        }

        progressInPercent =
                if (bytesTotal > 0) (bytesDownloaded * 100L / bytesTotal).toInt() else 0

        cursor.close();

        sendBroadcast(intent);

        mHandler.postDelayed(mRunnable, 100)
    }

    // Downloading logic
    private fun downloadFile() {
        val request =
                DownloadManager.Request(Uri.parse("https://www.pexels.com/video/6868420/download/?search_query=&tracking_id=bl9jjt6dnko"))
        request.setTitle("Voyager")
                .setDescription("File is downloading...")
                .setDestinationInExternalFilesDir(
                        this,
                        Environment.DIRECTORY_DOWNLOADS, fileName
                )
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadId = downloadManager.enqueue(request)
    }
}