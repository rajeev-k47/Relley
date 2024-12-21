package net.runner.relley.fn

import android.content.Context

fun logout(context: Context) {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.remove("github_access_token")
    editor.apply()
}
