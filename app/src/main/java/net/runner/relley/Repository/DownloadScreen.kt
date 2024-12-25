package net.runner.relley.Repository

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hitanshudhawan.circularprogressbar.CircularProgressBar
import net.runner.relley.Data.getStarredList
import net.runner.relley.Data.removeStarred
import net.runner.relley.Data.removedownloadUrl
import net.runner.relley.Data.saveStarred
import net.runner.relley.fn.downloadApk
import net.runner.relley.fn.installApk
import net.runner.relley.ui.theme.BG
import net.runner.relley.ui.theme.BottomNavigationIconUnselected

@Composable
fun DownloadScreen(context: Context,url:String,name:String,repo:Repository) {

    var downloading by rememberSaveable {
        mutableStateOf(false)
    }
    var progress by rememberSaveable {
        mutableIntStateOf(0)
    }
    var starStatus by rememberSaveable {
        mutableStateOf(if(getStarredList(context).contains(repo)) "Unstar APK" else "Star APK")
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(BG),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Card(
            modifier = Modifier
                .padding(horizontal = 40.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BottomNavigationIconUnselected.copy(alpha = 0.09f)),
            border = CardDefaults.outlinedCardBorder(true)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Found an APK", fontSize = 28.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, color = Color.LightGray, modifier = Modifier.padding(10.dp))
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    colors = CardDefaults.cardColors(containerColor = BottomNavigationIconUnselected.copy(alpha = 0.2f)),
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, color = Color.LightGray, modifier = Modifier.padding(10.dp))
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 10.dp)
                ) {

                    Button(onClick ={
                        if(!getStarredList(context).contains(repo)){
                            starStatus = "Unstar APK"
                            saveStarred(repo,context)
                        }else{
                            starStatus = "Star APK"
                            removeStarred(repo.id!!,context)
                            removedownloadUrl(context,repo.id!!)

                        }
                    },
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(end = 10.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BottomNavigationIconUnselected.copy(alpha = 0.1f)),
                    ) {
                        Text(starStatus, color = BottomNavigationIconUnselected, fontWeight =  FontWeight.Bold)
                    }
                    Button(onClick ={
                        downloading = true

                        downloadApk(context, url, "${repo.name}.apk", onProgress = {
                            pr->progress=pr
                        }) { file ->
                            downloading = false
                            file?.let {
                                installApk(context, it)
                            }
                        }
                    },
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(start = 10.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BottomNavigationIconUnselected.copy(alpha = 0.1f)),
                    ) {
                        Text(" Install APK", color = BottomNavigationIconUnselected, fontWeight =  FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        if(downloading){
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


    }
}
