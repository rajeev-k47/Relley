package net.runner.relley.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import net.runner.relley.Data.getStoredAccessToken
import net.runner.relley.R

@Composable
fun Intermediate(navController: NavController,authorized:Boolean){
    val context = LocalContext.current
    var showSplashScreen by remember { mutableStateOf(true) }

    Log.d("authhhh","authorized:$authorized")
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        val accessToken = getStoredAccessToken(context)
        if (accessToken == null) {
            Log.d("auth","accessToken is null")
            navController.navigate("Login") {
                popUpTo(0) { inclusive = true }
            }
        } else if(authorized){
            navController.navigate("Intermediate") {
                popUpTo(0) { inclusive = true }
            }
        }
        else{
            navController.navigate("Main") {
                popUpTo(0) { inclusive = true }
            }
        }
        showSplashScreen = false
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black)){
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(painter = painterResource(id = R.drawable.intermediateimage), contentDescription = "gitcat", tint = Color.White , modifier = Modifier
                .padding(top = 200.dp)
                .background(Color.Black)
                .size(150.dp))

            Text(text = "Relley", color = MaterialTheme.colorScheme.onBackground, fontSize = 45.sp, modifier = Modifier.padding(top = 205.dp, bottom = 15.dp ), fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(color = Color.Black, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 30.dp), trackColor = MaterialTheme.colorScheme.onBackground)
    }


}