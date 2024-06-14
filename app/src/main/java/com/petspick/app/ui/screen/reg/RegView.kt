package com.petspick.app.ui.screen.reg

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.petspick.app.R
import com.petspick.app.ui.elements.BackPressHandler
import com.petspick.app.ui.elements.DefButton
import com.petspick.app.ui.elements.DefText
import com.petspick.app.ui.elements.DefTextField
import com.petspick.app.ui.navigation.Screen
import com.petspick.app.ui.screen.main.MainViewModel
import com.petspick.app.ui.theme.Blue
import com.petspick.app.ui.theme.Gray
import com.petspick.app.ui.theme.LightGray
import com.petspick.app.ui.theme.LightGreen
import com.petspick.app.ui.theme.White
import com.petspick.app.ui.theme.cShape
import com.petspick.app.ui.theme.lPadding
import com.petspick.app.ui.theme.lText
import com.petspick.app.ui.theme.sPadding
import com.petspick.app.ui.theme.xlPadding
import com.petspick.app.utils.Const
import com.petspick.app.utils.ToastHelper

@Composable
fun RegView(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val activity = context as Activity

    UI(
        loginClick = {
            navController.navigate(Screen.Login.name)
        },
        regClick = { mail, pass, repPass, name ->
            mainViewModel.reg(activity, mail, pass, repPass, name){
                if (it != null) {
                    mainViewModel.getAnnouncement {  }
                    mainViewModel.getUsers()
                    mainViewModel.loadFavorites()
                    mainViewModel.getMessages()
                    navController.navigate(Screen.Main.name)
                }
            }
        },
    )
    BackPressHandler{
        navController.navigate(Screen.Login.name)
    }
}

@Composable
private fun UI(
    loginClick: () -> Unit = {},
    regClick: (String, String, String, String) -> Unit = {_, _, _, _ ->},
) {
    var name by remember { mutableStateOf("") }
    var repPass by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(White).verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(id = R.drawable.reg),
            contentDescription = Const.IMAGE_DESCRIPTION,
            modifier = Modifier.size(200.dp)
        )
        DefText(textId = R.string.registration, size = 40, weight = FontWeight.Bold)
        DefTextField(
            R.string.name,
            name,
            onValueChange = {
                name = it
            },
            modifier = Modifier.padding(start = xlPadding, top = lPadding, end = xlPadding)
        )
        DefTextField(
            R.string.mail,
            mail,
            onValueChange = {
                mail = it
            },
            modifier = Modifier.padding(start = xlPadding, top = lPadding, end = xlPadding)
        )
        DefTextField(
            R.string.pass,
            pass,
            onValueChange = {
                pass = it
            },
            isPass = true,
            modifier = Modifier.padding(start = xlPadding, top = lPadding, end = xlPadding)
        )
        DefTextField(
            R.string.repeatPass,
            repPass,
            onValueChange = {
                repPass = it
            },
            isPass = true,
            modifier = Modifier.padding(start = xlPadding, top = lPadding, end = xlPadding)
        )
        DefButton(
            R.string.reg,
            modifier = Modifier
                .padding(top = lPadding)
                .width(310.dp)
                .height(50.dp)
                .shadow(15.dp, RoundedCornerShape(cShape)),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGreen,
            )
        ) {
            regClick(mail, pass, repPass, name)
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = lPadding)
        ) {
            DefText(textId = R.string.existAcc, size = lText)
            DefText(textId = R.string.login_lc,
                size = lText,
                color = Blue,
                textDecoration = TextDecoration.Underline,
                weight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = sPadding)
                    .clickable { loginClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    UI()
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}