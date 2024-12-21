package net.runner.relley.ViewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.runner.relley.BuildConfig
import net.runner.relley.Data.getStoredAccessToken
import net.runner.relley.fn.Repository
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class RepoViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val _fetchedData = MutableLiveData<List<Repository>>()
    val fetchedData: LiveData<List<Repository>> get() = _fetchedData

    private val _accessToken = MutableLiveData<String?>()
    val accessToken: LiveData<String?> get() = _accessToken

    init {
        val token = getStoredAccessToken(context)
        if(token != null){

            _accessToken.postValue(token)
        }
    }


    fun getAccessToken(code: String) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("client_id", BuildConfig.GITHUB_CLIENT_ID)
            .add("client_secret", BuildConfig.GITHUB_CLIENT_SECRET)
            .add("code", code)
            .build()

        val request = Request.Builder()
            .url("https://github.com/login/oauth/access_token")
            .post(requestBody)
            .header("Accept","application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //failure
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful) {
                    val accessToken = JSONObject(responseBody).getString("access_token")
                    Log.d("auth","accessToken:$accessToken")
                    _accessToken.postValue(accessToken)
                }
            }
        })
    }

    fun fetchRepositories() {
        accessToken.observeForever { token ->
            if (!token.isNullOrEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val client = OkHttpClient()
                        val request = Request.Builder()
                            .url("https://api.github.com/user/repos")
                            .header("Authorization", "Bearer $token")
                            .build()

                        val response = client.newCall(request).execute()
                        if (response.isSuccessful) {
                            val responseBody = response.body?.string()
                            val listType = object : TypeToken<List<Repository>>() {}.type
                            val repositories = Gson().fromJson<List<Repository>>(responseBody, listType)
                            _fetchedData.postValue(repositories)
                            Log.d("auth","repositories:$repositories")
                        } else {
                            _fetchedData.postValue(emptyList())
                        }
                    } catch (e: Exception) {
                        Log.e("RepoFetchError","Error fetching repositories", e)
                        _fetchedData.postValue(emptyList())
                    }
                }
            }
        }
    }
}
