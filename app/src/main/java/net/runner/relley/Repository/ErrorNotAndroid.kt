package net.runner.relley.Repository

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.runner.relley.R
import net.runner.relley.ui.theme.BG

@Composable
fun ErrorNotAndroid(ErrorMessage:String){
    Column(
        modifier = Modifier.fillMaxSize().background(BG),
        verticalArrangement =  Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(painter = painterResource(id = R.drawable.android), contentDescription = "android", modifier = Modifier.padding(10.dp).size(150.dp), tint = MaterialTheme.colorScheme.surfaceTint)
        Text(text = "Error", fontSize = 38.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, color = Color.White, modifier = Modifier.padding(10.dp))
        Text(text = ErrorMessage,fontSize = 18.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
    }
}
