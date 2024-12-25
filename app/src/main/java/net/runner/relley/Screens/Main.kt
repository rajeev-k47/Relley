package net.runner.relley.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import net.runner.relley.Apps
import net.runner.relley.Commits
import net.runner.relley.R
import net.runner.relley.Repository.Repositories
import net.runner.relley.Repository.Repository
import net.runner.relley.fn.logout
import net.runner.relley.ui.theme.BG
import net.runner.relley.ui.theme.BottomNavigationIconUnselected

@Composable
fun Main(navController: NavController, repository: List<Repository>?=null, url:String?=null){
    val context = LocalContext.current
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf("Apps", "Repositories", "Commits")
    val selectedIcons = listOf(painterResource(id = R.drawable.app),painterResource(id = R.drawable.repository),painterResource(id = R.drawable.commit))
    val unselectedIcons =
        listOf(painterResource(id = R.drawable.app),painterResource(id = R.drawable.repository),painterResource(id = R.drawable.commit))
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = BG)){

        Scaffold(
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black)
                            )
                        )
                ) {

                    NavigationBar(
                        containerColor =Color.Transparent,
                    ){
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        painter = if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                        contentDescription = item,
                                        tint = if (selectedItem == index) Color.White else Color.Gray,
                                        modifier = Modifier.size(30.dp)
                                    )
                                },
                                label = { Text(item) },
                                selected = selectedItem == index,
                                onClick = { selectedItem = index },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BottomNavigationIconUnselected,
                                    selectedTextColor = Color.White,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray,
                                    indicatorColor = BottomNavigationIconUnselected.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
            },
            containerColor = Color.Transparent,
            topBar = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                ){
                    Column {

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ){
                        Text(text = "Relley", color = Color.White, fontSize = 25.sp, modifier = Modifier.padding(start = 10.dp), fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            AsyncImage(
                                model = url,
                                contentDescription = "avatar",
                                placeholder = rememberAsyncImagePainter(R.drawable.user),
                                error = rememberAsyncImagePainter(R.drawable.user),
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(35.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .clickable {
                                        navController.navigate("Login") {
                                            popUpTo(0) { inclusive = true }
                                        }
                                        logout(context)
                                    },
                            )
                        }
                    HorizontalDivider()
                    }
                }

            }
        ) {innerpadding ->

            when(selectedItem){
                0->{
                    Apps(navController,innerpadding)
                }
                1->{
                    Repositories(navController,repository,innerpadding)
                }
                2->{
                    Commits(navController,innerpadding)
                }
            }
        }
    }

}