package com.petspick.app.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.petspick.app.R
import com.petspick.app.ui.elements.BackPressHandler
import com.petspick.app.ui.screen.main.window.AnnouncementList
import com.petspick.app.ui.screen.main.window.Chat
import com.petspick.app.ui.screen.main.window.Favorite
import com.petspick.app.ui.screen.main.window.MyAnnouncementList
import com.petspick.app.ui.screen.main.window.NewAnnouncement
import com.petspick.app.ui.screen.main.window.Profile
import com.petspick.app.ui.theme.Black
import com.petspick.app.ui.theme.Purple
import com.petspick.app.ui.theme.UltraLightGray
import com.petspick.app.ui.theme.Violet
import com.petspick.app.ui.theme.White
import com.petspick.app.utils.Const

enum class Window {
    HOME,
    PAW,
    CHAT,
    PERSON,
    NEW,
    MY_LIST,
}

@Composable
fun MainView(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    UI(
        mainViewModel,
        navController
    )

    BackPressHandler {}
}

@Composable
private fun UI(
    mainViewModel: MainViewModel,
    navController: NavController = NavController(LocalContext.current)
) {
    val lastWindow = MainViewModel.lastWindow.collectAsState()
    var window by remember { mutableStateOf(lastWindow.value) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(UltraLightGray)
    ) {
        maxHeight

        when(window) {
            Window.PAW -> {
                MainViewModel.setLastWindow(Window.PAW)
                Favorite(openChat = {
                    MainViewModel.openChat.value = it
                    window = Window.CHAT
                })
            }
            Window.MY_LIST -> {
                MainViewModel.setLastWindow(Window.MY_LIST)
                MyAnnouncementList(mainViewModel) {
                    window = Window.NEW
                }
            }
            Window.NEW -> {
                NewAnnouncement(mainViewModel) {
                    window = Window.MY_LIST
                }
            }
            Window.CHAT -> {
                MainViewModel.setLastWindow(Window.CHAT)
                Chat()
            }
            Window.PERSON -> {
                MainViewModel.setLastWindow(Window.PERSON)
                Profile(
                    mainViewModel,
                    navController
                )
            }
            Window.HOME -> {
                MainViewModel.setLastWindow(Window.HOME)
                AnnouncementList(openChat = {
                    MainViewModel.openChat.value = it
                    window = Window.CHAT
                })
            }
        }
        Box (
            Modifier
                .height(80.dp)
                .align(Alignment.BottomCenter)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .background(Violet)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(70.dp)
            ) {
                SelectorIcon(R.drawable.home, R.drawable.home_fill, window, Window.HOME) {
                    window = Window.HOME
                }

                SelectorIcon(R.drawable.paw, R.drawable.paw_fill, window, Window.PAW) {
                    window = Window.PAW
                }

                Spacer(modifier = Modifier.size(50.dp))

                SelectorIcon(R.drawable.chat, R.drawable.chat_fill, window, Window.CHAT) {
                    window = Window.CHAT
                }

                SelectorIcon(R.drawable.person, R.drawable.person_fill, window, Window.PERSON) {
                    window = Window.PERSON
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .size(60.dp)
                .background(if (window == Window.NEW) Black else Purple, CircleShape)
        ){
            Icon(
                painterResource(id = R.drawable.add),
                Const.IMAGE_DESCRIPTION,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
                    .clickable {
                        window = Window.MY_LIST
                    },
                tint = White
            )
        }
    }

}

@Composable
private fun SelectorIcon(iconId: Int, fillId: Int, window: Window, currentWindow: Window, onClick: () -> Unit) {
    Icon(
        painterResource(id = if (window == currentWindow) fillId else iconId),
        Const.IMAGE_DESCRIPTION,
        modifier = Modifier
            .size(28.dp)
            .clickable {
                onClick()
            },
        tint = Black
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    UI(hiltViewModel())
}