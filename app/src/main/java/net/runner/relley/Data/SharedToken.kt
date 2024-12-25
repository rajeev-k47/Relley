package net.runner.relley.Data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.runner.relley.Repository.Repository

fun saveToken(accessToken:String,context:Context){
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("github_access_token", accessToken)
    editor.apply()

}
 fun getStoredAccessToken(context: Context): String? {
    try {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("github_access_token", null)
    }catch (e: Exception) {
        return null
    }
}
fun saveProfile(profileData:String,context:Context){
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("github_profile", profileData)
    editor.apply()

}
fun getProfile(context:Context):String? {
    try {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("github_profile", null)
    }catch (e: Exception) {
        return null
    }

}
fun saveStarred(repo: Repository, context: Context) {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()

    val starredReposJson = sharedPreferences.getString("github_starred_repos", "[]")
    val starredReposType = object : TypeToken<MutableList<Repository>>() {}.type
    val starredRepos: MutableList<Repository> = gson.fromJson(starredReposJson, starredReposType)

    if (!starredRepos.any { it.id == repo.id }) {
        starredRepos.add(repo)
    }
    editor.putString("github_starred_repos", gson.toJson(starredRepos))
    editor.apply()
}

fun removeStarred(repoId: Long, context: Context) {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()

    val starredReposJson = sharedPreferences.getString("github_starred_repos", "[]")
    val starredReposType = object : TypeToken<MutableList<Repository>>() {}.type
    val starredRepos: MutableList<Repository> = gson.fromJson(starredReposJson, starredReposType)

    val updatedRepos = starredRepos.filterNot { it.id == repoId }.toMutableList()

    editor.putString("github_starred_repos", gson.toJson(updatedRepos))
    editor.apply()
}


fun getStarredList(context: Context): List<Repository> {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val gson = Gson()
    val starredReposJson = sharedPreferences.getString("github_starred_repos", "[]")
    val starredReposType = object : TypeToken<List<Repository>>() {}.type
    return gson.fromJson(starredReposJson, starredReposType)
}

fun savedownloadUrl(context: Context,downloadUrl : String,id:Long){
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("github_download_url_$id", downloadUrl)
    editor.apply()
}
fun removedownloadUrl(context: Context,id:Long){
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.remove("github_download_url_$id")
    editor.apply()
}
fun getdownloadUrl(context: Context,id:Long):String? {
    try {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("github_download_url_$id", null)
    }catch (e: Exception) {
        return null
    }
}