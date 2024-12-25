package net.runner.relley.Repository

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.runner.relley.Data.getProfile
import net.runner.relley.Data.getStoredAccessToken
import net.runner.relley.Data.savedownloadUrl
import net.runner.relley.fn.getApkFile
import net.runner.relley.fn.getContent
import net.runner.relley.ui.theme.BG
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

@Composable
fun redirectApp(navcontroller: NavController,repository: Repository) {
    val context = LocalContext.current
    var loading = rememberSaveable { mutableStateOf(true) }
    var response = rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val token = rememberSaveable {
        mutableStateOf(getStoredAccessToken(context))
    }
    var showDownloadScreen by rememberSaveable { mutableStateOf(false) }
    var downloadUrl by rememberSaveable { mutableStateOf("") }
    var apkName by rememberSaveable { mutableStateOf("") }
    LaunchedEffect (key1 = repository.name){
           getContent(context = context, token = token.value!!, repository = repository){ responseBody, error->
               response.value=responseBody
               loading.value=false

           }

    }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(BG))
        {
            if(loading.value){
                LinearProgressIndicator(modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(bottom = 50.dp), color = Color.White, trackColor = Color.Transparent)
            }else if (showDownloadScreen) {
                DownloadScreen(context = context, url = downloadUrl, name = apkName, repo = repository)
            } else{
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                   if(isAndroidProject(response.value)){
                       LaunchedEffect(key1 = response.value) {
                           getApkFile(
                               context = context,
                               token = token.value!!,
                               repository = repository
                           ) { apk,url,error ->
                               if (error.isEmpty()) {
                                   downloadUrl = url
                                   apkName = apk
                                   showDownloadScreen = true
                               } else {
                                   errorMessage = error
                               }
                           }
                       }

                   }
                   else{
                       errorMessage = "Seems like this is not an Android project."

                   }
                    errorMessage?.let {
                        ErrorNotAndroid(it)
                    }
                }
            }

        }
}

fun isAndroidProject(responseBody: String): Boolean {
    val jsonArray =JSONArray(responseBody)
    for(i in 0 until jsonArray.length()){
        if(jsonArray[i] is JSONObject) {
            val jsonObject = jsonArray[i] as JSONObject
            val name = jsonObject.getString("name")
            if(name=="app"){
                return true
            }
        }
    }
    return false
}
fun getApkIcon(context: Context, apkFile: File): Drawable? {
    try {
        val packageManager= context.packageManager
        val packageInfo= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageArchiveInfo(apkFile.absolutePath, PackageManager.GET_ACTIVITIES)
        } else {
            packageManager.getPackageArchiveInfo(apkFile.absolutePath, 0)
        }
        packageInfo?.let {
            val appIcon: Drawable = packageManager.getApplicationIcon(it.applicationInfo!!)
            return appIcon
        }
    } catch (e: Exception) {
        Log.e("APKIcon", "Error icon", e)
    }
    return null
}