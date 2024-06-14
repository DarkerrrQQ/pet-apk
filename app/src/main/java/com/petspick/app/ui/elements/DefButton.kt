package com.petspick.app.ui.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.petspick.app.ui.theme.Black
import com.petspick.app.ui.theme.Gray
import com.petspick.app.ui.theme.Violet
import com.petspick.app.ui.theme.White
import com.petspick.app.ui.theme.buttonHeight
import com.petspick.app.ui.theme.lText
import com.petspick.app.ui.theme.mText
import com.petspick.app.ui.theme.nonScaledSp
import com.petspick.app.ui.theme.smallButtonWidth

@Composable
fun DefButton(
    textId: Int,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Violet,
    ),
    shape: Dp = 20.dp,
    textSize: Int = lText,
    enabled: Boolean = true,
    textColor: Color = Black,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = colors,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(shape),
        enabled = enabled,
        modifier = modifier
            .width(smallButtonWidth)
            .height(buttonHeight)
    ) {
        Text(stringResource(id = textId), color = textColor, fontSize = textSize.nonScaledSp)
    }
}

@Composable
fun DefButton(
    textId: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Gray,
    ),
    textColor: Color = White,
    textSize: Int = mText,
    shape: Dp = 20.dp,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = colors,
        shape = RoundedCornerShape(shape),
        contentPadding = PaddingValues(0.dp),
        enabled = enabled,
        modifier = modifier
            .width(smallButtonWidth)
            .height(buttonHeight)
    ) {
        Text(textId, color = textColor, fontSize = textSize.nonScaledSp)
    }
}