package com.github.se.signify.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R

@Composable
fun ReusableButtonWithIcon(onClickAction: () -> Unit, icon: ImageVector, iconDescription: String) {
  OutlinedButton(
      onClick = onClickAction,
      modifier = Modifier.padding(8.dp),
      border =
          ButtonDefaults.outlinedButtonBorder.copy(
              width = 2.dp, brush = SolidColor(colorResource(R.color.dark_gray))),
      colors = ButtonDefaults.outlinedButtonColors(colorResource(R.color.blue)),
  ) {
    Icon(icon, tint = colorResource(R.color.dark_gray), contentDescription = iconDescription)
  }
}

@Composable
fun ReusableTextButton(
    onClickAction: () -> Unit,
    text: String,
) {
  OutlinedButton(
      onClick = onClickAction,
      border =
          ButtonDefaults.outlinedButtonBorder.copy(
              width = 2.dp, brush = SolidColor(colorResource(R.color.dark_gray))),
      colors = ButtonDefaults.buttonColors(colorResource(R.color.blue)),
      modifier = Modifier.fillMaxWidth().height(40.dp)) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.dark_gray),
            fontSize = 16.sp)
      }
}
