package net.runner.relley.fn

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.runner.relley.Data.getProfile
import net.runner.relley.Data.savedownloadUrl
import net.runner.relley.Repository.Repository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


suspend fun getApkFile(
    context: Context,
    token: String,
    repository: Repository,
    onresult :(String,String,String)->Unit
) {
    val apkName = mutableStateOf("")
    val downloadUrl = mutableStateOf("")
    val errorMessage = mutableStateOf("")


        try {
            val profile = getProfile(context)
            val username = profile?.let {JSONObject(it).getString("login") }.toString()
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.github.com/repos/${username}/${repository.name}/contents/app/release")
                .header("Authorization", "Bearer $token")
                .header("Accept", "application/json")
                .build()

            val apiResponse = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
            if (apiResponse.isSuccessful) {
                val responseBody = apiResponse.body?.string()
                var apkAvailable = false
                var apkAvailableObject: JSONObject = JSONObject()
                val jsonArray = JSONArray(responseBody)

                for (i in 0 until jsonArray.length()) {
                    if (jsonArray[i] is JSONObject) {
                        val jsonObject = jsonArray[i] as JSONObject
                        val name = jsonObject.getString("name")
                        if (name.endsWith(".apk")) {
                            apkAvailable = true
                            apkAvailableObject = jsonObject
                            apkName.value = name
                            break
                        }
                    }
                }
                if (apkAvailable) {
                    downloadUrl.value = apkAvailableObject.getString("download_url")
                    savedownloadUrl(context, downloadUrl.value, repository.id!!)
                    onresult(apkName.value, downloadUrl.value,"")
                } else {
                    errorMessage.value ="Releases does not contain APK release !!"
                    onresult("","", errorMessage.value)
                }
            } else {
                errorMessage.value ="No APK release found !!"
                onresult("","", errorMessage.value)
            }
        } catch (e: Exception) {
            errorMessage.value = "An error occurred:$e"
            onresult("","", errorMessage.value)

        }
}

suspend fun getContent(context: Context, token: String, repository: Repository,onresult: (String, String) -> Unit) {
    try {
        val profile = getProfile(context)
        val username = profile?.let {JSONObject(it).getString("login") }.toString()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.github.com/repos/${username}/${repository.name}/contents")
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/json")
            .build()

        val apiResponse = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }
        if (apiResponse.isSuccessful) {
            val responseBody = apiResponse.body?.string()
            onresult(responseBody.toString(), "")
        }
    }catch (e: Exception) {
        onresult("", e.toString())
    }
}