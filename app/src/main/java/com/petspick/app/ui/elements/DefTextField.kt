package com.petspick.app.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.petspick.app.ui.theme.Black
import com.petspick.app.ui.theme.Gray
import com.petspick.app.ui.theme.Transparent
import com.petspick.app.ui.theme.cShape
import com.petspick.app.ui.theme.lText
import com.petspick.app.ui.theme.mPadding

@Composable
fun DefTextField(
    titleId: Int,
    text: String,
    textSize: Int = lText,
    colorText: Color = Black,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Bold,
    enabled: Boolean = true,
    isPass: Boolean = false,
    isNumber: Boolean = false,
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {}
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            DefText(textId = titleId, size = textSize, weight = fontWeight, color = colorText, modifier = Modifier.padding(start = mPadding, end = mPadding))
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Gray,
            unfocusedContainerColor = Gray,
            focusedTextColor = Black,
            unfocusedTextColor = Black,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            disabledContainerColor = Gray,
            disabledTextColor = Black,
            disabledIndicatorColor = Transparent
        ),
        enabled = enabled,
        singleLine = true,
        visualTransformation = if (isPass) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPass) KeyboardType.Password else if (isNumber) KeyboardType.Number else KeyboardType.Text),
        maxLines = 1,
        modifier = modifier
            .padding(top = mPadding)
            .fillMaxWidth()
            .shadow(15.dp, RoundedCornerShape(cShape))
            .clip(RoundedCornerShape(cShape))
            .clickable {
                onClick()
            }
    )
}