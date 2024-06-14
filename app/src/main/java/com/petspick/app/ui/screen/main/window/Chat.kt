package com.petspick.app.ui.screen.main.window

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.petspick.app.R
import com.petspick.app.api.Message
import com.petspick.app.api.UserInfo
import com.petspick.app.data.storage.models.Announcement
import com.petspick.app.ui.elements.AnnouncementItem
import com.petspick.app.ui.elements.DefText
import com.petspick.app.ui.elements.DefTextField
import com.petspick.app.ui.screen.main.MainViewModel
import com.petspick.app.ui.theme.Black
import com.petspick.app.ui.theme.Blue
import com.petspick.app.ui.theme.Gray
import com.petspick.app.ui.theme.Red
import com.petspick.app.ui.theme.lPadding
import com.petspick.app.utils.Const
import com.petspick.app.utils.Utils
import java.util.UUID

@Composable
fun Chat(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val openChat = MainViewModel.openChat.collectAsState()
    val list = MainViewModel.messageList

    LaunchedEffect(Unit) {
        mainViewModel.getMessages()
    }

    val myMessagesList = list.value.filter { it.from == MainViewModel.user!!.uid || it.to == MainViewModel.user!!.uid}

    if (openChat.value.id.isNotEmpty()) {
        Dialog(
            announcement = openChat.value,
            send = {
                mainViewModel.setMessage(it)
                mainViewModel.getMessages()
            },
            back = {
                MainViewModel.openChat.value = Announcement()
            }
        )
    } else {
        Body(myMessagesList){
            MainViewModel.openChat.value = it
        }
    }
}

@Composable
private fun Dialog(
    announcement: Announcement,
    send: (Message) -> Unit,
    back: () -> Unit = {}
) {
    val user = MainViewModel.userInfo.collectAsState()

    val messageList = MainViewModel.messageList.value.filter {
        it.announcement == announcement.name && ( it.to == user.value.id || it.from == user.value.id )
    }.sortedBy { it.date }
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(bottom = 180.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(messageList.size){
                val otherUser = MainViewModel.usersList.value.firstOrNull { elem -> elem.id == messageList[it].from && elem.id != user.value.id }

                Row (
                    Modifier
                        .padding(vertical = 5.dp)
                        .fillMaxWidth()
                        .background(Gray)
                        .width(60.dp)
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (messageList[it].from == user.value.id) Arrangement.End else Arrangement.Start
                ){
                    if (messageList[it].from == user.value.id)
                        DefText(textId = messageList[it].message, size = 18, modifier = Modifier.padding(end = 10.dp))
                    if (messageList[it].from == user.value.id && user.value.image.isNotEmpty()) {
                        Image(
                            bitmap = Utils.base64ToBitmap(user.value.image).asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(2.dp, Color.Transparent, RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    } else if (otherUser != null && otherUser.image.isNotEmpty()){
                        Image(
                            bitmap = Utils.base64ToBitmap(otherUser.image).asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(2.dp, Color.Transparent, RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Box (
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .background(Blue, CircleShape),
                        ){

                        }
                    }
                    if (messageList[it].from != user.value.id)
                        DefText(textId = messageList[it].message, size = 18, modifier = Modifier.padding(start = 10.dp))
                }
            }
        }
        Row(
            Modifier
                .clickable {
                    back()
                }
                .fillMaxWidth()
                .height(70.dp)
                .background(Gray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = R.drawable.back),
                Const.IMAGE_DESCRIPTION,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp),
                tint = Black
            )
            if (announcement.image.isNotEmpty()) {
                Image(
                    bitmap = Utils.base64ToBitmap(announcement.image).asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Transparent, CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box (
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .background(Red, CircleShape),
                ){

                }
            }
            Column (
                Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                DefText(textId = user.value.name)
                Row {
                    DefText(textId = announcement.name, weight = FontWeight.Bold)
                    DefText(textId = "${announcement.price}р", modifier = Modifier.padding(start = 10.dp))
                }

            }
        }
        Row(
            Modifier
                .padding(bottom = 90.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            Spacer(modifier = Modifier.width(55.dp))
            DefTextField(
                R.string.message,
                message,
                onValueChange = {
                    message = it
                },
                modifier = Modifier
                    .width(230.dp)
            )
            Box (
                Modifier
                    .padding(top = 15.dp, start = 10.dp)
                    .background(Gray, CircleShape)
                    .size(55.dp)
            ){
                Icon(
                    painterResource(id = R.drawable.send),
                    Const.IMAGE_DESCRIPTION,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                        .clickable {
                            val otherUser =
                                MainViewModel.usersList.value.firstOrNull { it.id == announcement.userId }
                            if (otherUser != null) {
                                send(
                                    Message(
                                        id = UUID
                                            .randomUUID()
                                            .toString(),
                                        from = user.value.id,
                                        to = otherUser.id,
                                        message = message,
                                        announcement = announcement.name,
                                        date = System
                                            .currentTimeMillis()
                                            .toString()
                                    )
                                )
                                message = ""
                            }
                        },
                    tint = Black
                )
            }

        }
    }

}

@Composable
private fun Body(
    list: List<Message>,
    open: (Announcement) -> Unit = {},
) {
    val groupList = list.groupBy { it.announcement }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        DefText(textId = R.string.messages, size = 25, weight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopCenter))

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 60.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(groupList.size) {
                val key = groupList.keys.elementAt(it)
                val item = groupList.getValue(key)[0]

                val annoList = MainViewModel.announcementList.value
                val announcement =
                    annoList.firstOrNull { elem -> elem.name == item.announcement }

                val otherUser =
                    if (announcement != null)
                        MainViewModel.usersList.value.firstOrNull { it.id == announcement.userId }
                    else
                        UserInfo()

                Row (
                    Modifier
                        .background(Gray, RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            if (announcement != null) open(announcement)
                        }
                ){

                    if (announcement != null && announcement.image.isNotEmpty()) {
                        Image(
                            bitmap = Utils.base64ToBitmap(announcement.image).asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(2.dp, Color.Transparent, RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Box (
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .background(Red, RoundedCornerShape(20.dp)),
                        ){

                        }
                    }

                    Column (
                        Modifier
                            .padding(top = 20.dp, start = 5.dp, bottom = 20.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceAround
                    ){
                        DefText(
                            textId = if (otherUser != null && otherUser.name.isNotEmpty()) otherUser.name else "Имя"
                        )
                        DefText(textId =
                            if (announcement != null && announcement.name.isNotEmpty()) announcement.name else "Название товара",
                            size = 20
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 200.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Chat()
}