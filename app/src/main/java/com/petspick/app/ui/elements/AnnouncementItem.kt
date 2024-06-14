package com.petspick.app.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petspick.app.data.storage.models.Announcement
import com.petspick.app.ui.theme.Gray
import com.petspick.app.ui.theme.Red
import com.petspick.app.ui.theme.lPadding
import com.petspick.app.utils.Utils

@Composable
fun AnnouncementItem(
    item: Announcement,
    open: (Announcement) -> Unit = {}
) {
    Column(Modifier
        .width(270.dp)
        .background(Gray, RoundedCornerShape(30.dp))
        .clickable {
            open(item)
        }
    ) {
        if (item.image.isNotEmpty()) {
            Image(
                bitmap = Utils.base64ToBitmap(item.image).asImageBitmap(),
                contentDescription = "",
                modifier = Modifier
                    .width(270.dp)
                    .height(240.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(2.dp, Color.Transparent, RoundedCornerShape(30.dp)),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box (
                modifier = Modifier
                    .width(270.dp)
                    .height(240.dp)
                    .background(Red, RoundedCornerShape(30.dp)),
            ){

            }
        }
        DefText(textId = item.name, size = 20, weight = FontWeight.Bold,
            modifier = Modifier.padding(start = lPadding, top = 10.dp, end = lPadding))
        DefText(textId = "Адрес: ${item.address}", modifier = Modifier.padding(start = lPadding, top = 10.dp, end = lPadding))
        DefText(textId = "Пол: ${item.sex}", modifier = Modifier.padding(start = lPadding, top = 10.dp, end = lPadding))
        DefText(textId = "Возраст: ${item.age} мес", modifier = Modifier.padding(start = lPadding, top = 10.dp, end = lPadding))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            DefText(textId = "${item.price}р", size = 20, weight = FontWeight.Bold,
                modifier = Modifier.padding(start = lPadding, top = 10.dp, end = lPadding, bottom = lPadding))
        }
    }
}