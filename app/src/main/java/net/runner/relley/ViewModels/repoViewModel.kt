package net.runner.relley.ViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.runner.relley.BuildConfig
import net.runner.relley.Data.getStoredAccessToken
import net.runner.relley.Data.saveProfile
import net.runner.relley.Repository.Repository
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

    private val _profile = MutableLiveData<String?>()
    val profile: LiveData<String?> get() = _profile

    init {
        val token = getStoredAccessToken(context)
        if(token != null){
            Log.d("auth","toekn$token")
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
                    try {
                        val accessToken = JSONObject(responseBody).getString("access_token")
                        _accessToken.postValue(accessToken)
                    }catch (e: Exception) {
                        Log.e("AccessTokenError","Error getting access token", e)
                }}
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
                            withContext(Dispatchers.Main) {
                                _fetchedData.value = repositories
//                                _fetchedData.postValue(repositories)
                            }
                            Log.d("auth","repositories:$repositories")
                        } else {
                            _fetchedData.postValue(emptyList())
                            Log.d("auth","repositories:${response.body?.string()}")

                        }

                    } catch (e: Exception) {
                        Log.e("RepoFetchError","Error fetching repositories", e)
                        _fetchedData.postValue(emptyList())
                    }
                }
            }
        }
    }
    fun profile(){
        accessToken.observeForever { token ->
            if (!token.isNullOrEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val client1 = OkHttpClient()
                        val request1 = Request.Builder()
                            .url("https://api.github.com/user")
                            .header("Authorization", "Bearer $token")
                            .build()

                        val response1 = client1.newCall(request1).execute()
                        if (response1.isSuccessful) {
                            val responseBody1 = response1.body?.string()
                            Log.d("auth", "user:$responseBody1")
                            val jsonObject = JSONObject(responseBody1)
                            val avatarUrl = jsonObject.getString("avatar_url")
                            saveProfile(responseBody1!!,context)
                            withContext(Dispatchers.Main){

                                _profile.value=avatarUrl
                            }

//                            _profile.postValue(avatarUrl)
                        } else {
                            _profile.postValue("")

                        }
                    } catch (e: Exception) {
                        _profile.postValue("")

                    }
                }
            }
        }}
}

