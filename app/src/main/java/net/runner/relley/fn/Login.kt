package net.runner.relley.fn

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.core.content.ContextCompat.startActivity
import net.runner.relley.BuildConfig

fun Redirect(context: Context){
    val authUrl = "https://github.com/login/oauth/authorize?client_id=${BuildConfig.GITHUB_CLIENT_ID}&redirect_uri=relley://callback&scope=repo,user"
    val intent = Intent(Intent.ACTION_VIEW, authUrl.toUri())
    startActivity(context, intent, null)

}