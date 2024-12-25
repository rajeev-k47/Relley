package net.runner.relley

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.hitanshudhawan.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.runner.relley.Data.getStarredList
import net.runner.relley.Data.getStoredAccessToken
import net.runner.relley.Data.getdownloadUrl
import net.runner.relley.Data.removeStarred
import net.runner.relley.Data.removedownloadUrl
import net.runner.relley.Repository.Repository
import net.runner.relley.fn.downloadApk
import net.runner.relley.fn.getApkFile
import net.runner.relley.fn.getContent
import net.runner.relley.fn.installApk
import net.runner.relley.ui.theme.BottomNavigationIconUnselected

@Composable
fun Apps(navController: NavController,innerpadding: PaddingValues) {
    val context = LocalContext.current
    val starred = rememberSaveable {
        mutableStateOf(getStarredList(context))
    }
    repoCard(repository = starred.value,innerpadding, context ){updatedList->
        starred.value = updatedList
    }
}
@Composable
fun repoCard(repository:List<Repository>,innerpadding: PaddingValues,context: Context,onUpdate: (List<Repository>) -> Unit){
    var downloadStates by rememberSaveable {
        mutableStateOf(repository.associate { it.id!! to Triple(false,false, 0) })
    }
    val coroutine = CoroutineScope(Dispatchers.IO)
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .padding(innerpadding),
        columns = GridCells.Adaptive(minSize = LocalConfiguration.current.screenWidthDp.dp / 2-10.dp)
    ) {
        items(repository.size) { index ->
            val (isDownloading,isRefreshing, progress) = downloadStates[repository[index].id!!] ?: Triple(false,false, 0)

            Card (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(7.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardColors(
                    containerColor = BottomNavigationIconUnselected.copy(alpha = 0.13f),
                    contentColor = Color.White,
                    disabledContainerColor = BottomNavigationIconUnselected.copy(alpha = 0.09f),
                    disabledContentColor = Color.White
                )
            ){
                Column (
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        contentAlignment = Alignment.Center
                    ){

                        Text(text = repository[index].name!!, fontSize = 22.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, color = BottomNavigationIconUnselected, modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .align(Alignment.Center)
                            , overflow = TextOverflow.Ellipsis, maxLines = 1,

                        )
                        Icon(
                            painter = painterResource(id = R.drawable.refresh), contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 10.dp)
                                .clip(
                                    RoundedCornerShape(20.dp)
                                )
                                .size(22.dp)
                                .clickable {
                                    coroutine.launch {
                                        downloadStates = downloadStates.toMutableMap().apply {
                                            this[repository[index].id!!] = Triple(false,true, 0)
                                        }
                                        getApkFile(
                                            context,
                                            getStoredAccessToken(context)!!,
                                            repository[index]
                                        ) { apk, Url, errorMessage ->
                                            if (errorMessage.isEmpty()) {
                                                downloadStates = downloadStates.toMutableMap().apply {
                                                    this[repository[index].id!!] = Triple(false,false, 0)
                                                }
                                            } else {
                                                Log.d("error", errorMessage)
                                            }

                                        }


                                    }

                                },
                            tint = BottomNavigationIconUnselected.copy(alpha = 0.9f),
                        )
                    }
                    AsyncImage(
                        model = repository[index].owner!!.avatar_url,
                        contentDescription = "avatar",
                        placeholder = rememberAsyncImagePainter(R.drawable.android),
                        error = rememberAsyncImagePainter(R.drawable.android),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(58.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){

                        Button(onClick ={
                                removeStarred(repoId = repository[index].id!!,context)
                                 removedownloadUrl(context,repository[index].id!!)
                            onUpdate(repository.filter { it.id != repository[index].id })

                        },
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(start = 10.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BottomNavigationIconUnselected.copy(alpha = 0.1f)),
                        ) {
                            Icon(painter = painterResource(id = R.drawable.star), contentDescription = "star", tint = BottomNavigationIconUnselected, modifier = Modifier.size(25.dp))
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Button(onClick ={
                            if(isDownloading) return@Button
                            var url = getdownloadUrl(context,repository[index].id!!)
                            url?.let{url->
                                downloadStates = downloadStates.toMutableMap().apply {
                                    this[repository[index].id!!] = Triple(true,false, 0)
                                }
                                downloadApk(context, url, "${repository[index].name}.apk", onProgress = {
                                        pr-> downloadStates = downloadStates.toMutableMap().apply {
                                    this[repository[index].id!!] = Triple(true,false, pr)
                                }
                                }) { file ->
                                    downloadStates = downloadStates.toMutableMap().apply {
                                        this[repository[index].id!!] = Triple(false,false, 0)
                                    }
                                    file?.let {
                                        installApk(context, it)
                                    }
                                }
                            }
                        },
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(end = 10.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BottomNavigationIconUnselected.copy(alpha = 0.1f)),
                        ) {
                            Icon(painter = painterResource(id = R.drawable.install), contentDescription = "star", tint = BottomNavigationIconUnselected, modifier = Modifier.size(22.dp))

                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    if(isRefreshing){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Refreshing...", fontSize = 18.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, color = Color.LightGray, modifier = Modifier.padding(10.dp))
                        }
                    }
                    if(isDownloading){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Downloading...", fontSize = 18.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, color = Color.LightGray, modifier = Modifier.padding(10.dp))
                            CircularProgressBar(
                                modifier = Modifier.size(60.dp),
                                progress = progress.toFloat(),
                                progressMax = 100f,
                                progressBarColor = BottomNavigationIconUnselected.copy(alpha = 0.8f),
                                progressBarWidth = 10.dp,
                                backgroundProgressBarColor = BottomNavigationIconUnselected.copy(alpha = 0.1f),
                                backgroundProgressBarWidth = 10.dp,
                                roundBorder = true,
                                startAngle = 0f
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))

                }
            }

        }
    }
}