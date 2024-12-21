package net.runner.relley

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import net.runner.relley.ui.theme.RelleyTheme
import net.runner.relley.fn.getAccessToken

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent?.data
        Log.d("auth", "onCreate URI: $uri")
        uri?.getQueryParameter("code")?.let { code ->
            getAccessToken(code)
        }
        enableEdgeToEdge()
        setContent {
            RelleyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Login()
                }
            }
        }
    }


}
