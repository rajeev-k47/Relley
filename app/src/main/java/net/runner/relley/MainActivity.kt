package net.runner.relley

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.runner.relley.Data.getStoredAccessToken
import net.runner.relley.Data.saveToken
import net.runner.relley.Repository.Repository
import net.runner.relley.Repository.redirectApp
import net.runner.relley.Screens.Intermediate
import net.runner.relley.Screens.Login
import net.runner.relley.Screens.Main
import net.runner.relley.ViewModels.RepoViewModel
import net.runner.relley.ui.theme.RelleyTheme

class MainActivity : ComponentActivity() {
    private lateinit var repoViewModel: RepoViewModel
    private var repository : List<Repository>? = null
    private var avatar_url : String? = null
    private var authorized: Boolean = false


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
        uri?.getQueryParameter("code")?.let{code ->
            if(getStoredAccessToken(this) == null) {
                authorized=true
                repoViewModel.getAccessToken(code)

                val authUrl = "https://github.com/apps/rellley/installations/new"
                val intent = Intent(Intent.ACTION_VIEW, authUrl.toUri())
                ContextCompat.startActivity(this, intent, null)
            }
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
                        Intermediate(navController,authorized)
                    }
                    composable(route = "Main") {
                        Main(navController,repository,avatar_url)
                    }
                    composable(route="Login") {
                        Login(navController)
                    }
                    composable(
                        route="redirectApp/{repo}",
                        arguments = listOf(
                            navArgument("repo") {
                                type = NavType.StringType
                            }
                        ) )
                    {navBackStackEntry ->
                        val repoId= navBackStackEntry.arguments?.getString("repo")
                        val repo = repository?.filter { it.id == repoId?.toLong() }
                        redirectApp(navController, repo!![0])


                    }
                }
            }

        }
    }


}
