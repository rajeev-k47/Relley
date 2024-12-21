package net.runner.relley.Repository

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import net.runner.relley.R
import net.runner.relley.fn.Repository
import net.runner.relley.ui.theme.BottomNavigationIconUnselected

@Composable
fun Repositories(navController: NavController,repository: List<Repository>?=null,innerpadding:PaddingValues){

    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerpadding)
    ){
        repository?.forEach {
            item {
                RepositoryItem(navController = navController, repository = it)
            }
        }
    }
}
@Composable
fun RepositoryItem(navController: NavController, repository: Repository){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ){
        AsyncImage(
            model = repository.owner!!.avatar_url,
            contentDescription = "avatar",
            placeholder = rememberAsyncImagePainter(R.drawable.ic_launcher_foreground),
            error = rememberAsyncImagePainter(R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .padding(10.dp)
                .size(60.dp)
                .clip(RoundedCornerShape(15.dp))
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp)
            .weight(1f)
        ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){

                        Text(text = repository.name!!, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier
                            .padding(end = 5.dp))
                        if(repository.private!!){
                            Icon(imageVector = Icons.Default.Lock , contentDescription = "Private", tint = Color.LightGray, modifier = Modifier
                                .padding(start = 5.dp)
                                .size(15.dp))
                        }
                        if(repository.fork!!){
                            Icon(painter = painterResource(id = R.drawable.fork) , contentDescription = "Fork", tint = Color.LightGray, modifier = Modifier
                                .padding(start = 5.dp)
                                .size(15.dp))
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                    }

                    Text(text = "@${repository.owner!!.login!!}", color =BottomNavigationIconUnselected, fontSize = 15.sp)
                }

            }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(end = 14.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BottomNavigationIconUnselected.copy(alpha = 0.1f)),
        ) {
            Text(text = "View", color = BottomNavigationIconUnselected, fontWeight =  FontWeight.Bold)
        }


    }
}