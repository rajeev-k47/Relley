package net.runner.relley.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Github
import net.runner.relley.R
import net.runner.relley.fn.Redirect

@Composable
fun Login(navController: NavController) {
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxSize()
        .paint(
            painterResource(id = R.drawable.bgimg),
            contentScale = ContentScale.FillBounds
        ))
    {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Welcome", color = MaterialTheme.colorScheme.onBackground, fontSize = 40.sp, modifier = Modifier.padding(top = 200.dp, bottom = 5.dp ))
            HorizontalDivider(modifier = Modifier.fillMaxWidth(0.4f).padding(bottom = 240.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(4f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                ,
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { Redirect(context) },
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Brands.Github,
                        contentDescription = "Github",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                    )
                    Text(
                        text = "Login with Github",
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 16.sp
                    )
                }
            }
        }

    }
}