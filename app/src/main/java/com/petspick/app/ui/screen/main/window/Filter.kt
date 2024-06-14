package com.petspick.app.ui.screen.main.window

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petspick.app.R
import com.petspick.app.ui.elements.DefButton
import com.petspick.app.ui.elements.DefText
import com.petspick.app.ui.elements.DefTextField
import com.petspick.app.ui.theme.Black
import com.petspick.app.ui.theme.LightGreen
import com.petspick.app.ui.theme.Red
import com.petspick.app.ui.theme.cShape
import com.petspick.app.ui.theme.lPadding
import com.petspick.app.ui.theme.xlPadding

@Composable
fun Filter(apply: (FilterParam) -> Unit = {}, cancel: () -> Unit = {}) {

    var city by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var ageFrom by remember { mutableStateOf("") }
    var ageTo by remember { mutableStateOf("") }
    var priceFrom by remember { mutableStateOf("") }
    var priceTo by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        DefText(
            textId = R.string.enterParam, size = 25, weight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp)
        )

        DefTextField(
            R.string.city,
            city,
            onValueChange = {
                city = it
            },
            modifier = Modifier.padding(start = xlPadding, top = lPadding, end = xlPadding)
        )
        DefTextField(
            R.string.sex,
            sex,
            onValueChange = {
                sex = it
            },
            modifier = Modifier.padding(start = xlPadding, top = lPadding, end = xlPadding)
        )
        DefText(
            textId = R.string.age, size = 20, weight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DefTextField(
                R.string.from,
                ageFrom,
                onValueChange = {
                    ageFrom = it
                },
                isNumber = true,
                modifier = Modifier.width(120.dp)
            )
            DefTextField(
                R.string.to,
                ageTo,
                onValueChange = {
                    ageTo = it
                },
                isNumber = true,
                modifier = Modifier.width(120.dp)
            )
        }
        DefText(
            textId = R.string.price, size = 20, weight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DefTextField(
                R.string.from,
                priceFrom,
                onValueChange = {
                    priceFrom = it
                },
                isNumber = true,
                modifier = Modifier.width(120.dp)
            )
            DefTextField(
                R.string.to,
                priceTo,
                onValueChange = {
                    priceTo = it
                },
                isNumber = true,
                modifier = Modifier.width(120.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 80.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DefButton(
                R.string.apply,
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
                apply(
                    FilterParam(
                        city,
                        sex,
                        ageFrom,
                        ageTo,
                        priceFrom,
                        priceTo
                    )
                )
            }
            DefButton(
                R.string.cancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red,
                ),
                textColor = Black,
                modifier = Modifier
                    .padding(top = lPadding)
                    .width(120.dp)
                    .height(50.dp)
                    .shadow(15.dp, RoundedCornerShape(cShape))
            ) {
                cancel()
            }
        }
    }
}

data class FilterParam(
    var city: String = "",
    var sex: String = "",
    var ageFrom: String = "",
    var ageTo: String = "",
    var priceFrom: String = "",
    var priceTo: String = "",
)