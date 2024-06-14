package com.petspick.app.ui.screen.main.window

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.petspick.app.R
import com.petspick.app.data.storage.models.Announcement
import com.petspick.app.ui.elements.AnnouncementItem
import com.petspick.app.ui.elements.DefButton
import com.petspick.app.ui.elements.DefText
import com.petspick.app.ui.screen.main.MainViewModel
import com.petspick.app.ui.theme.LightGreen
import com.petspick.app.ui.theme.cShape

@Composable
fun MyAnnouncementList(
    mainViewModel: MainViewModel,
    new: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        mainViewModel.getAnnouncement{}
    }

    var announcement by remember { mutableStateOf(Announcement()) }

    if (announcement.id.isNotEmpty()) {
        Card(
            announcement = announcement,
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
            }
        )
    } else {
        Body(
            new = new,
            open = {
                announcement = it
            }
        )
    }

}
@Composable
private fun Body(new: () -> Unit = {}, open: (Announcement) -> Unit = {}) {
    val list = MainViewModel.myAnnouncementList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        DefText(textId = R.string.myAnnouncement, size = 25, weight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopCenter))

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 60.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(list.value.size) {
                AnnouncementItem(item = list.value[it]) { elem ->
                    open(elem)
                }
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = 200.dp))
            }
        }

        DefButton(
            R.string.newAnnouncement,
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGreen,
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .width(300.dp)
                .height(50.dp)
                .shadow(15.dp, RoundedCornerShape(cShape))
        ) {
            new()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyAnnouncementList(hiltViewModel())
}