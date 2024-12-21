package net.runner.relley

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.runner.relley.Data.saveToken
import net.runner.relley.Screens.Intermediate
import net.runner.relley.Screens.Login
import net.runner.relley.ViewModels.RepoViewModel
import net.runner.relley.Screens.Main
import net.runner.relley.fn.Repository
import net.runner.relley.ui.theme.RelleyTheme

class MainActivity : ComponentActivity() {
    private lateinit var repoViewModel: RepoViewModel
    private var repository : List<Repository>? = null
    private var avatar_url : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoViewModel = ViewModelProvider(this)[RepoViewModel::class.java]
        repoViewModel.accessToken.observe(this, Observer { token ->
            if (!token.isNullOrEmpty()) {
                saveToken(token, this)
                repoViewModel.fetchRepositories()
                repoViewModel.profile()
            }
        })
        repoViewModel.fetchedData.observe(this, Observer { repositories ->
            if (repositories.isEmpty()) {
                //empty
            } else {
                repository = repositories

            }
        })
        repoViewModel.profile.observe(this, Observer { url ->
            avatar_url =url
        })

        val uri = intent?.data
        Log.d("auth", "onCreateURI:$uri")
        uri?.getQueryParameter("code")?.let{code ->
            Log.d("auth","onCreateCode:$code")
            repoViewModel.getAccessToken(code)
        }
        enableEdgeToEdge()
        setContent {
            RelleyTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "Intermediate"
                ) {
                    composable(route = "InterMediate") {
                        Intermediate(navController)
                    }
                    composable(route = "Main") {
                        Main(navController,repository,avatar_url)
                    }
                    composable(route="Login") {
                        Login(navController)
                    }
                }
            }

        }
    }


}
