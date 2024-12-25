package net.runner.relley.fn

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import okhttp3.*
import java.io.File
import java.io.IOException

fun downloadApk(
    context: Context,
    url: String,
    fileName: String,
    onProgress: (Int) -> Unit,
    onComplete: (File?) -> Unit
) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            onComplete(null)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val contentLength = response.body?.contentLength() ?: -1
                val file = File(context.cacheDir, fileName)
                file.outputStream().use { output ->
                    val inputStream = response.body?.byteStream()
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    var totalBytesRead: Long = 0
                    while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                        totalBytesRead += bytesRead
                        output.write(buffer, 0, bytesRead)
                        val progress = if (contentLength > 0) {
                            ((totalBytesRead * 100) / contentLength).toInt()
                        } else {
                            -1
                        }
                        onProgress(progress)
                    }
                }
                onComplete(file)
            } else {
                onComplete(null)
            }
        }
    })
}
fun installApk(context: Context, apkFile: File) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val packageManager = context.packageManager
        if (!packageManager.canRequestPackageInstalls()) {
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            intent.data = Uri.parse("package:${context.packageName}")
            startActivity(context,intent,null)
        }
    }

    val apkUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        apkFile
    )
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(apkUri, "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(intent)
}
