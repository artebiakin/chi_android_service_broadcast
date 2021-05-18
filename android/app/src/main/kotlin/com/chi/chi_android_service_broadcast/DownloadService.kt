package com.chi.chi_android_service_broadcast

import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.chi.chi_android_service_broadcast.models.Download
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import java.io.*

class DownloadService : JobIntentService() {
    companion object {
        const val RESULT = "com.chi.chi_android_service_broadcast.result"
        const val NOTIFICATION = "com.chi.chi_android_service_broadcast.notification"
    }

    private var totalFileSize = 0


    override fun onHandleWork(intent: Intent) {
        initDownload()
    }

    private fun initDownload() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.pexels.com/")
                .build()
        val retrofitInterface: RetrofitInterface = retrofit.create(RetrofitInterface::class.java)
        val request: Call<ResponseBody> = retrofitInterface.downloadFile()
        try {
            request.execute().body()?.let { downloadFile(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun downloadFile(body: ResponseBody) {
        var count: Int = 0
        val data = ByteArray(1024 * 4)
        val fileSize = body.contentLength()
        val bis: InputStream = BufferedInputStream(body.byteStream(), 1024 * 8)
        val outputFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "file.zip"
        )
        val output: OutputStream = FileOutputStream(outputFile)
        var total: Long = 0
        val startTime = System.currentTimeMillis()
        var timeCount = 1
        while (bis.read(data).also({ count = it }) != -1) {
            total += count.toLong()
            totalFileSize = (fileSize / Math.pow(1024.0, 2.0)).toInt()
            val current = Math.round(total / Math.pow(1024.0, 2.0)).toDouble()
            val progress = (total * 100 / fileSize).toInt()
            val currentTime = System.currentTimeMillis() - startTime
            val download = Download()
            download.totalFileSize = totalFileSize
            if (currentTime > 1000 * timeCount) {
                download.currentFileSize = current.toInt()
                download.progress = progress
                sendNotification(download)
                timeCount++
            }
            output.write(data, 0, count)
        }
        onDownloadComplete()
        output.flush()
        output.close()
        bis.close()
    }

    private fun sendNotification(download: Download) {
        sendIntent(download)
    }

    private fun sendIntent(download: Download) {
        val intent = Intent(NOTIFICATION)
        intent.putExtra(RESULT, download)
        LocalBroadcastManager.getInstance(this@DownloadService).sendBroadcast(intent)
    }

    private fun onDownloadComplete() {
        val download = Download()
        download.progress = 100
        totalFileSize = 0
        sendIntent(download)
    }


    override fun onTaskRemoved(rootIntent: Intent) {
    }

}