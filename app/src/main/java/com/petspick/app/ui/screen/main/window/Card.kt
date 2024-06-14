package com.petspick.app.ui.screen.main.window

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.petspick.app.R
import com.petspick.app.data.storage.models.Announcement
import com.petspick.app.ui.elements.DefButton
import com.petspick.app.ui.elements.DefTextField
import com.petspick.app.ui.screen.main.MainViewModel
import com.petspick.app.ui.theme.Black
import com.petspick.app.ui.theme.Blue
import com.petspick.app.ui.theme.Gray
import com.petspick.app.ui.theme.LightGreen
import com.petspick.app.ui.theme.Red
import com.petspick.app.ui.theme.White
import com.petspick.app.ui.theme.cShape
import com.petspick.app.ui.theme.lPadding
import com.petspick.app.utils.Const
import com.petspick.app.utils.Utils

@Composable
fun Card(
    announcement: Announcement,
    openChat: (Announcement) -> Unit = {},
    call: (Announcement) -> Unit = {},
    save: (Announcement) -> Unit = {},
    delete: (Announcement) -> Unit = {},
    favorite: (Announcement, Boolean) -> Unit = { _, _ ->},
    back: () -> Unit = {}
) {
    val context = LocalContext.current

    val favoriteList = MainViewModel.favoritesList
    val myList = MainViewModel.myAnnouncementList
    val favorite = favoriteList.value.firstOrNull{it.id == announcement.id}
    val myItem = myList.value.firstOrNull{it.id == announcement.id}
    var isFavorite by remember { mutableStateOf(favorite != null) }

    var image by remember { mutableStateOf(announcement.image) }
    var name by remember { mutableStateOf(if (myItem != null) announcement.name else "Имя: ${announcement.name}",) }
    var sex by remember { mutableStateOf(if (myItem != null) announcement.sex else "Пол: ${announcement.sex}") }
    var age by remember { mutableStateOf(if (myItem != null) announcement.age else "Возраст: ${announcement.age}") }
    var desc by remember { mutableStateOf(if (myItem != null) announcement.description else "Описание: ${announcement.description}") }
    var price by remember { mutableStateOf(if (myItem != null) announcement.price else "Цена: ${announcement.price}р") }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null)
            Utils.uriToBitmap(context, uri) {
                image = Utils.bitmapToBase64(it)
            }
    }

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painterResource(id = R.drawable.back),
                    Const.IMAGE_DESCRIPTION,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            back()
                        },
                    tint = Black
                )
                Row {
                    Icon(
                        painterResource(id = R.drawable.link),
                        Const.IMAGE_DESCRIPTION,
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(30.dp)
                            .clickable {

                            },
                        tint = Black
                    )
                    Icon(
                        painterResource(id = if (!isFavorite) R.drawable.paw else R.drawable.paw_fill),
                        Const.IMAGE_DESCRIPTION,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                isFavorite = !isFavorite
                                favorite(announcement, isFavorite)
                            },
                        tint = Black
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(300.dp)
                    .padding(top = 20.dp)
                    .background(Gray, RoundedCornerShape(30.dp)),
            ) {
                if (announcement.image.isNotEmpty()) {
                    Image(
                        bitmap =
                        Utils.base64ToBitmap(image).asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(30.dp))
                            .clickable {
                                if (myItem != null) {
                                    launcher.launch("image/*")
                                }
                            }
                            .border(2.dp, Color.Transparent, RoundedCornerShape(30.dp)),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.photo),
                        Const.IMAGE_DESCRIPTION,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
                                if (myItem != null) {
                                    launcher.launch("image/*")
                                }
                            }
                            .size(60.dp),
                        tint = Black
                    )
                }
            }

            DefTextField(
                R.string.name,
                name,
                enabled = myItem != null,
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 5.dp),
                onValueChange = {
                    name = it
                }
            )
            DefTextField(
                R.string.sex,
                sex,
                enabled = myItem != null,
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 5.dp),
                onValueChange = {
                    sex = it
                }
            )
            DefTextField(
                R.string.age,
                age,
                isNumber = true,
                enabled = myItem != null,
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 5.dp),
                onValueChange = {
                    age = it
                }
            )
            DefTextField(
                R.string.description,
                desc,
                enabled = myItem != null,
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 5.dp),
                onValueChange = {
                    desc = it
                }
            )
            DefTextField(
                R.string.price,
                price,
                isNumber = true,
                enabled = myItem != null,
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 5.dp),
                onValueChange = {
                    price = it
                }
            )
            Spacer(modifier = Modifier.padding(bottom = 150.dp))
        }

        if (myItem != null) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp, start = 45.dp, end = 45.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefButton(
                    R.string.save,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightGreen,
                    ),
                    textColor = Black,
                    modifier = Modifier
                        .padding(top = lPadding)
                        .width(120.dp)
                        .height(50.dp)
                        .shadow(15.dp, RoundedCornerShape(cShape))
                ) {
                    save(Announcement(
                        id = announcement.id,
                        userId = announcement.userId,
                        name = name,
                        sex = sex,
                        address = announcement.address,
                        age = age,
                        type = announcement.type,
                        description = desc,
                        price = price,
                        image = image
                    ))
                }
                DefButton(
                    R.string.delete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red,
                    ),
                    textColor = White,
                    modifier = Modifier
                        .padding(top = lPadding)
                        .width(120.dp)
                        .height(50.dp)
                        .shadow(15.dp, RoundedCornerShape(cShape))
                ) {
                    delete(announcement)
                }
            }
        } else {
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp, start = 45.dp, end = 45.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefButton(
                    R.string.call,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightGreen,
                    ),
                    textColor = Black,
                    modifier = Modifier
                        .padding(top = lPadding)
                        .width(120.dp)
                        .height(50.dp)
                        .shadow(15.dp, RoundedCornerShape(cShape))
                ) {
                    call(announcement)
                }
                DefButton(
                    R.string.write,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                    ),
                    textColor = White,
                    modifier = Modifier
                        .padding(top = lPadding)
                        .width(120.dp)
                        .height(50.dp)
                        .shadow(15.dp, RoundedCornerShape(cShape))
                ) {
                    openChat(announcement)
                }
            }
        }
    }
}