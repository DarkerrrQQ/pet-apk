package com.petspick.app.ui.screen.main.window

import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.petspick.app.R
import com.petspick.app.api.UserInfo
import com.petspick.app.ui.elements.DefButton
import com.petspick.app.ui.elements.DefText
import com.petspick.app.ui.elements.DefTextField
import com.petspick.app.ui.navigation.Screen
import com.petspick.app.ui.screen.main.MainViewModel
import com.petspick.app.ui.theme.Black
import com.petspick.app.ui.theme.Blue
import com.petspick.app.ui.theme.Gray
import com.petspick.app.ui.theme.LightGreen
import com.petspick.app.ui.theme.Red
import com.petspick.app.ui.theme.Violet
import com.petspick.app.ui.theme.White
import com.petspick.app.ui.theme.cShape
import com.petspick.app.ui.theme.lPadding
import com.petspick.app.ui.theme.lText
import com.petspick.app.ui.theme.sPadding
import com.petspick.app.ui.theme.xlPadding
import com.petspick.app.utils.Const
import com.petspick.app.utils.ToastHelper
import com.petspick.app.utils.Utils
import com.petspick.app.utils.Utils.Companion.bitmapToBase64
import com.petspick.app.utils.Utils.Companion.uriToBitmap

@Composable
fun Profile(
    mainViewModel: MainViewModel,
    navController: NavController = NavController(LocalContext.current)
) {
    val context = LocalContext.current
    val userInfo = MainViewModel.userInfo

    var image by remember { mutableStateOf(userInfo.value.image) }
    var name by remember { mutableStateOf(userInfo.value.name) }
    var phone by remember { mutableStateOf(userInfo.value.phone) }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null)
            uriToBitmap(context, uri) {
                image = bitmapToBase64(it)
            }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DefText(textId = R.string.profile, size = 25, weight = FontWeight.Bold)

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
            modifier = Modifier
                .width(300.dp)
                .padding(top = lPadding)
        )
        DefTextField(
            R.string.phone,
            phone,
            isNumber = true,
            onValueChange = {
                phone = it
            },
            modifier = Modifier
                .width(300.dp)
                .padding(top = lPadding)
        )
        DefButton(
            R.string.update,
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGreen,
            ),
            modifier = Modifier
                .padding(top = lPadding)
                .width(300.dp)
                .height(50.dp)
                .shadow(15.dp, RoundedCornerShape(cShape))
        ) {
            userInfo.value.image = image
            userInfo.value.name = name
            userInfo.value.phone = phone
            mainViewModel.updateUser(userInfo.value)
            ToastHelper().show(context, context.getString(R.string.toastUpdateSuccess))
        }
        DefButton(
            R.string.logout,
            colors = ButtonDefaults.buttonColors(
                containerColor = Red,
            ),
            textColor = White,
            modifier = Modifier
                .padding(top = lPadding)
                .width(300.dp)
                .height(50.dp)
                .shadow(15.dp, RoundedCornerShape(cShape))
        ) {
            mainViewModel.logout()
            navController.navigate(Screen.Login.name)
        }

        Spacer(modifier = Modifier.padding(bottom = 100.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Profile(hiltViewModel())
}