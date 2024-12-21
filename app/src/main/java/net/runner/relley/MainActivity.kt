package net.runner.relley

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.runner.relley.Data.saveToken
import net.runner.relley.ViewModels.RepoViewModel
import net.runner.relley.ui.theme.RelleyTheme

class MainActivity : ComponentActivity() {
    private lateinit var repoViewModel: RepoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoViewModel = ViewModelProvider(this)[RepoViewModel::class.java]
        repoViewModel.accessToken.observe(this, Observer { token ->
            if (!token.isNullOrEmpty()) {
                saveToken(token, this)
                repoViewModel.fetchRepositories()
            }
        })
        repoViewModel.fetchedData.observe(this, Observer { repositories ->
            if (repositories.isEmpty()) {
                //empty
            } else {
                //show repositories in UI

            }
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val fetchedData by repoViewModel.fetchedData.observeAsState(emptyList())
                    Log.d("fetchedData", fetchedData.toString())
                    if(fetchedData.isEmpty()){
                        Login()
                    }else{

                    }
                }
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "Main"
                ) {
                    composable(route = "Main") {
//                        Main()
                    }
                    composable(route="Login") {
                        Login()
                    }
                }
            }

        }
    }


}
