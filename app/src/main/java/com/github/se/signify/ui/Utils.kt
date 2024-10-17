package com.github.se.signify.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.github.se.signify.R

/**
 * A reusable composable function that creates an outlined button with an icon.
 *
 * @param onClickAction A lambda function to execute when the button is clicked.
 * @param icon The ImageVector representing the icon to be displayed in the button.
 * @param iconDescription A string description of the icon, used for accessibility and testing.
 */
@Composable
fun ReusableButtonWithIcon(onClickAction: () -> Unit, icon: ImageVector, iconDescription: String) {
  OutlinedButton(
      onClick = onClickAction,
      modifier = Modifier.padding(8.dp).testTag(iconDescription + "Button"),
      border =
          ButtonDefaults.outlinedButtonBorder.copy(
              width = 2.dp, brush = SolidColor(colorResource(R.color.dark_gray))),
      colors = ButtonDefaults.outlinedButtonColors(colorResource(R.color.blue)),
  ) {
    Icon(icon, tint = colorResource(R.color.dark_gray), contentDescription = iconDescription)
  }
}

/**
 * A reusable composable function that creates an outlined button with customizable text.
 *
 * @param onClickAction A lambda function to execute when the button is clicked.
 * @param textTag A string used for testing, which serves as the tag for the button.
 * @param text The text to be displayed inside the button.
 * @param height The height of the button.
 * @param borderColor The color of the button's border.
 * @param backgroundColor The background color of the button.
 * @param textSize The size of the text displayed inside the button.
 * @param textColor The color of the text inside the button.
 */
@Composable
fun ReusableTextButton(
    onClickAction: () -> Unit,
    textTag: String,
    text: String,
    height: Dp,
    borderColor: Color,
    backgroundColor: Color,
    textSize: TextUnit,
    textColor: Color,
) {
  OutlinedButton(
      onClick = onClickAction,
      border =
          ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp, brush = SolidColor(borderColor)),
      colors = ButtonDefaults.buttonColors(backgroundColor),
      modifier = Modifier.fillMaxWidth().height(height).testTag(textTag)) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontSize = textSize,
            textAlign = TextAlign.Center)
      }
}

@Composable
fun SquareButton(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    size: Dp,
    iconSize: Dp, // Icon size passed here
    labelFontSize: TextUnit,
    iconTint: Color,
    textColor: Color,
    iconOffset: Dp = 0.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
  Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier =
          modifier
              .size(size) // Button size
              .border(2.dp, Color.Black, RoundedCornerShape(12.dp)) // Black border
              .background(Color(0xFF05A9FB), RoundedCornerShape(12.dp)) // Blue inside
              .padding(16.dp)
              .clickable { onClick() }) {

        // Text on top of the icon
        Text(
            text = label,
            fontSize = labelFontSize,
            color = textColor,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Icon in the center of the square
        Box(contentAlignment = Alignment.Center) {
          Icon(
              painter = painterResource(id = iconRes),
              contentDescription = label,
              tint = iconTint, // Set icon tint to dark gray
              modifier =
                  Modifier.size(iconSize) // Applying the icon size
                      .offset(x = iconOffset) // Use the offset if specified
              )
        }
      }
}

@Composable
fun UtilButton(
    onClick: () -> Unit,
    testTagButton: String,
    testTagIcon: String,
    icon: ImageVector,
    contentDescription: String
) {
  Box(
      modifier =
          Modifier.size(30.dp)
              .background(Color.White, shape = RoundedCornerShape(30))
              .border(2.dp, Color.White, shape = RoundedCornerShape(30))
              .clickable { onClick() }
              .testTag(testTagButton),
      contentAlignment = Alignment.Center) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = colorResource(R.color.blue),
            modifier = Modifier.size(30.dp).testTag(testTagIcon))
      }
}

@Composable
fun BackButton(onClick: () -> Unit) {
  Box(
      modifier = Modifier.padding(16.dp) // Optional padding to adjust positioning
      ) {
        IconButton(
            onClick = { onClick() },
            modifier =
                Modifier.align(Alignment.TopStart)
                    .testTag("BackButton") // Align to the top left corner
            ) {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "BackButton",
                  tint = colorResource(R.color.blue))
            }
      }
}
