package com.petspick.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val mText: Int  = 14
val lText: Int = 16

val sPadding = 5.dp
val mPadding = 10.dp
val lPadding = 20.dp
val xlPadding = 40.dp

val cShape = 50.dp

//Button
val smallButtonWidth = 150.dp
val buttonHeight = 40.dp

val Int.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp


