package net.runner.relley.fn

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import net.runner.relley.BuildConfig

fun Redirect(context: Context){
    val authUrl = "https://github.com/login/oauth/authorize?client_id=${BuildConfig.GITHUB_CLIENT_ID}&scope=repo&&redirect_uri=relley://callback"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
    startActivity(context, intent, null)

}