package com.petspick.app.ui.screen.main.window

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
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
import com.petspick.app.ui.elements.DefButton
import com.petspick.app.ui.elements.DefText
import com.petspick.app.ui.elements.DefTextField
import com.petspick.app.ui.elements.PickerBottomSheetType
import com.petspick.app.ui.screen.main.MainViewModel
import com.petspick.app.ui.theme.Black
import com.petspick.app.ui.theme.Gray
import com.petspick.app.ui.theme.LightGreen
import com.petspick.app.ui.theme.cShape
import com.petspick.app.ui.theme.lPadding
import com.petspick.app.utils.Const
import com.petspick.app.utils.ToastHelper
import com.petspick.app.utils.Utils
import com.petspick.app.utils.Utils.Companion.bitmapToBase64
import com.petspick.app.utils.Utils.Companion.uriToBitmap

@Composable
fun NewAnnouncement(
    mainViewModel: MainViewModel,
    sent: () -> Unit = {}
) {
    val context = LocalContext.current

    var image by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null)
            uriToBitmap(context, uri) {
                image = bitmapToBase64(it)
            }
    }

    val list = listOf("Кошка", "Собака")
    var showBottomSheet by remember { mutableStateOf(false) }

    PickerBottomSheetType(
        showBottomSheet,
        list,
        callback = {
            type = it
            showBottomSheet = false
        },
        closeEvent = {
            showBottomSheet = false
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DefText(textId = R.string.createAnnouncement, size = 25, weight = FontWeight.Bold)

        Box(
            modifier = Modifier
                .size(300.dp)
                .padding(top = 20.dp)
                .background(Gray, RoundedCornerShape(30.dp))
                .clickable {
                    launcher.launch("image/*")
                },
        ) {
            if (image.isNotEmpty()) {
                Image(
                    bitmap =
                    Utils.base64ToBitmap(image).asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(30.dp))
                        .border(2.dp, Color.Transparent, RoundedCornerShape(30.dp))
                        .clickable {
                            launcher.launch("image/*")
                        },
                    contentScale = ContentScale.Crop,
                )
            } else {
                Icon(
                    painterResource(id = R.drawable.photo),
                    Const.IMAGE_DESCRIPTION,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp),
                    tint = Black
                )
            }
        }

        DefTextField(
            R.string.name,
            name,
            onValueChange = {
                name = it
            },
            modifier = Modifier.width(300.dp).padding(top = lPadding)
        )
        DefTextField(
            R.string.sex,
            sex,
            onValueChange = {
                sex = it
            },
            modifier = Modifier.width(300.dp).padding(top = lPadding)
        )
        DefTextField(
            R.string.age,
            age,
            isNumber = true,
            onValueChange = {
                age = it
            },
            modifier = Modifier.width(300.dp).padding(top = lPadding)
        )
        DefTextField(
            R.string.type,
            type,
            enabled = false,
            onValueChange = {
                type = it
            },
            onClick = {
                showBottomSheet = true
            },
            modifier = Modifier.width(300.dp).padding(top = lPadding)
        )
        DefTextField(
            R.string.city,
            city,
            onValueChange = {
                city = it
            },
            modifier = Modifier.width(300.dp).padding(top = lPadding)
        )
        DefTextField(
            R.string.description,
            desc,
            onValueChange = {
                desc = it
            },
            modifier = Modifier.width(300.dp).padding(top = lPadding)
        )
        DefTextField(
            R.string.price,
            price,
            isNumber = true,
            onValueChange = {
                price = it
            },
            modifier = Modifier.width(300.dp).padding(top = lPadding)
        )
        DefButton(
            R.string.sent,
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGreen,
            ),
            modifier = Modifier
                .padding(top = 25.dp)
                .width(300.dp)
                .height(50.dp)
                .shadow(15.dp, RoundedCornerShape(cShape))
        ) {
            mainViewModel.setAnnouncement(
                Announcement(
                    name = name,
                    sex = sex,
                    address = city,
                    age = age,
                    type = type,
                    description = desc,
                    price = price,
                    image = image
                )
            )
            ToastHelper().show(context, context.getString(R.string.toastNewAnnouncement))
            sent()
        }
        Spacer(modifier = Modifier.padding(bottom = 100.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    NewAnnouncement(hiltViewModel())
}