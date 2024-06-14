package com.petspick.app.ui.screen.load

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.petspick.app.R
import com.petspick.app.api.FirebaseRepository
import com.petspick.app.ui.elements.BackPressHandler
import com.petspick.app.ui.elements.DefText
import com.petspick.app.ui.navigation.Screen
import com.petspick.app.ui.screen.main.MainViewModel
import com.petspick.app.ui.theme.White
import com.petspick.app.utils.Const
import kotlinx.coroutines.delay

@Composable
fun LoadView(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        mainViewModel.getAnnouncement {  }
        mainViewModel.getUsers()
        mainViewModel.loadFavorites()
        mainViewModel.getMessages()
    }

    Countdown(1000L) {
        FirebaseRepository.checkAuth{
            if (it != null) {
                navController.navigate(Screen.Main.name)
                mainViewModel.getUser{}
            }
            else navController.navigate(Screen.Login.name)
        }
    }

    UI()

    BackPressHandler{}
}

@Composable
private fun UI() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.reg),
            contentDescription = Const.IMAGE_DESCRIPTION,
            modifier = Modifier.size(300.dp)
        )
        DefText(textId = R.string.title, size = 40, weight = FontWeight.Bold)
    }
}

@Composable
fun Countdown(targetTime: Long, endEvent: () -> Unit) {
    var remainingTime by remember(targetTime) {
        mutableLongStateOf(targetTime - System.currentTimeMillis())
    }

    LaunchedEffect(remainingTime) {
        delay(targetTime)
        remainingTime = targetTime - System.currentTimeMillis()
        endEvent()
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    UI()
}