package net.runner.relley.Data

import android.content.Context
import android.util.Log

fun saveToken(accessToken:String,context:Context){
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("github_access_token", accessToken)
    editor.apply()

}
 fun getStoredAccessToken(context: Context): String? {
    try {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        Log.d("auth","getStoredAccessToken:${sharedPreferences.getString("github_access_token", null)}")
        return sharedPreferences.getString("github_access_token", null)
    }catch (e: Exception) {
        return null
    }
}