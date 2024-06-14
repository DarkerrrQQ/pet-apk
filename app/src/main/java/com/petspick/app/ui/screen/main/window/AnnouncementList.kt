package com.petspick.app.ui.screen.main.window

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.petspick.app.R
import com.petspick.app.data.storage.models.Announcement
import com.petspick.app.ui.elements.AnnouncementItem
import com.petspick.app.ui.elements.DefButton
import com.petspick.app.ui.elements.DefText
import com.petspick.app.ui.elements.DefTextField
import com.petspick.app.ui.navigation.Screen
import com.petspick.app.ui.screen.main.MainViewModel
import com.petspick.app.ui.screen.main.Window
import com.petspick.app.ui.theme.Black
import com.petspick.app.ui.theme.Blue
import com.petspick.app.ui.theme.Gray
import com.petspick.app.ui.theme.LightGreen
import com.petspick.app.ui.theme.Red
import com.petspick.app.ui.theme.Violet
import com.petspick.app.ui.theme.White
import com.petspick.app.ui.theme.cShape
import com.petspick.app.ui.theme.lPadding
import com.petspick.app.ui.theme.xlPadding
import com.petspick.app.utils.Const
import com.petspick.app.utils.ToastHelper
import com.petspick.app.utils.Utils

@Composable
fun AnnouncementList(
    mainViewModel: MainViewModel = hiltViewModel(),
    openChat: (Announcement) -> Unit = {},
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        mainViewModel.getAnnouncement{}
    }

    var announcement by remember { mutableStateOf( Announcement() ) }
    var filterParam by remember { mutableStateOf(FilterParam()) }
    var lastType by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    if (announcement.id.isNotEmpty()) {
        Card(
            announcement,
            openChat = openChat,
            call = {
                val users = MainViewModel.usersList.value
                val user = users.firstOrNull{ elem -> elem.id == it.id }
                if (user != null) {
                    mainViewModel.callPhone(context, user.phone)
                } else {
                    ToastHelper().show(context, context.getString(R.string.toastUsersDontExist))
                }
            },
            favorite = { item, isFavorite ->
                if (isFavorite) {
                    mainViewModel.insertFavorites(item)
                } else {
                    mainViewModel.deleteFavorites(item)
                }
            },
            save = {
                mainViewModel.setAnnouncement(it)
                mainViewModel.getAnnouncement { }
                announcement = Announcement()
            },
            delete = {
                mainViewModel.removeAnnouncement(it)
                mainViewModel.getAnnouncement { }
                announcement = Announcement()
            },
            back = {
                announcement = Announcement()
                type = lastType
            }
        )
    } else if (type == "filter"){
        Filter(
            apply = {
                filterParam = it
                type = lastType
            },
            cancel = {
                type = lastType
            }
        )
    } else if (type.isNotEmpty()) {
        List(
            type = type,
            filterParam = filterParam,
            filter = {
                type = "filter"
            },
            open = {
                announcement = it
            },
            back = {
                type = ""
            }
        )
    } else {
        TypeChoice {
            type = it
            lastType = type
        }
    }

}

@Composable
private fun TypeChoice(type: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DefText(textId = R.string.petsType, size = 25, weight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp))

        Column(
            Modifier
                .padding(top = 10.dp)
                .width(270.dp)
                .background(Gray, RoundedCornerShape(30.dp))
                .clickable {
                    type(Const.CAT)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.cat),
                contentDescription = "",
                modifier = Modifier
                    .width(200.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(2.dp, Color.Transparent, RoundedCornerShape(30.dp)),
                contentScale = ContentScale.Crop,
            )
            DefText(textId = R.string.cats, size = 30, weight = FontWeight.Bold,
                modifier = Modifier.padding(start = lPadding, top = 10.dp, end = lPadding, bottom = 10.dp))
        }
        Column(
            Modifier
                .width(270.dp)
                .padding(top = 10.dp)
                .background(Gray, RoundedCornerShape(30.dp))
                .clickable {
                    type(Const.DOG)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.dog),
                contentDescription = "",
                modifier = Modifier
                    .width(200.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(2.dp, Color.Transparent, RoundedCornerShape(30.dp)),
                contentScale = ContentScale.Crop,
            )
            DefText(textId = R.string.dogs, size = 30, weight = FontWeight.Bold,
                modifier = Modifier.padding(start = lPadding, top = 10.dp, end = lPadding, bottom = 10.dp))
        }
    }
}

@Composable
private fun List(
    type: String,
    filterParam: FilterParam,
    filter: () -> Unit = {},
    open: (Announcement) -> Unit = {},
    back: () -> Unit = {}
) {
    val list = MainViewModel.announcementList

    var filteredList = list.value.filter { it.type == type }
    if (filterParam.city.isNotEmpty()) {
        filteredList = filteredList.filter { it.address.lowercase() == filterParam.city.lowercase() }
    }
    if (filterParam.sex.isNotEmpty()) {
        filteredList = filteredList.filter { it.sex.lowercase() == filterParam.sex.lowercase() }
    }
    if (filterParam.ageTo.isNotEmpty()) {
        filteredList = filteredList.filter { it.age.toInt() <= filterParam.ageTo.toInt() }
    }
    if (filterParam.ageFrom.isNotEmpty()) {
        filteredList = filteredList.filter { it.age.toInt() >= filterParam.ageFrom.toInt() }
    }
    if (filterParam.priceTo.isNotEmpty()) {
        filteredList = filteredList.filter { it.price.toInt() <= filterParam.priceTo.toInt() }
    }
    if (filterParam.priceFrom.isNotEmpty()) {
        filteredList = filteredList.filter { it.price.toInt() >= filterParam.priceFrom.toInt() }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            Modifier.fillMaxWidth()
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
        }
        DefButton(
            R.string.setParam,
            colors = ButtonDefaults.buttonColors(
                containerColor = Violet,
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
                .width(300.dp)
                .height(50.dp)
                .shadow(15.dp, RoundedCornerShape(cShape))
        ) {
            filter()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 120.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(filteredList.size) {
                AnnouncementItem(item = filteredList[it], open = open)
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
    AnnouncementList()
}

