package com.shubhamvashishth.lenscorp.todo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shubhamvashishth.lenscorp.todo.ui.common.BottomBar
import com.shubhamvashishth.lenscorp.todo.ui.navigation.SetupNavGraph
import com.shubhamvashishth.lenscorp.todo.ui.theme.SmartTaskManagerTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SmartTaskManagerTheme {
               MainScreen()
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController) },
        topBar = {
            TopAppBar(navController = navController)
        },
                floatingActionButton = {

                FloatingActionButton(
                    onClick = {
                        // Handle FAB click
                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }

        }

    ) {
            paddingValues ->
        // Apply padding manually
        Box(modifier = Modifier.padding(top = 50.dp)) {
            SetupNavGraph(navHostController = navController)
        }
    }
}

@Composable
fun TopAppBar(navController: NavHostController){
    var ok by remember{ mutableStateOf("") }
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .height(50.dp)
        .fillMaxWidth()){
        Column{
            Text(text = "ReadPin",  fontSize = 30.sp,
                fontWeight = FontWeight.Bold,)
            //  TextField(value = ok, onValueChange = { ok= it } )
        }
    }
}