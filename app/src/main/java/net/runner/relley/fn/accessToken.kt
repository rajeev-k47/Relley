package net.runner.relley.fn

import android.util.Log
import net.runner.relley.BuildConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

 fun getAccessToken(code: String) {
     Log.d("authtoken", code)
    val client = OkHttpClient()
    val requestBody = FormBody.Builder()
        .add("client_id", BuildConfig.GITHUB_CLIENT_ID)
        .add("client_secret", BuildConfig.GITHUB_CLIENT_SECRET)
        .add("code", code)
        .build()

    val request = Request.Builder()
        .url("https://github.com/login/oauth/access_token")
        .post(requestBody)
        .header("Accept", "application/json")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {

        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            val accessToken = JSONObject(responseBody).getString("access_token")
            Log.d("authtoken", accessToken)
        }
    })
}
