package com.petspick.app.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petspick.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerBottomSheetType(
    showBottomSheet: Boolean,
    list: List<String>,
    callback: (String) -> Unit,
    closeEvent: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(false)

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                closeEvent()
            },
            containerColor = White,
            sheetState = sheetState
        ) {

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                items(list.size) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                callback(list[it])
                            }
                            .padding(10.dp)
                    ) {
                        DefText(
                            textId = list[it], size = 18, weight = FontWeight.Bold
                        )
                    }
                }

            }
        }
    }
}