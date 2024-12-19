package com.github.se.signify.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A button with an icon.
 *
 * @param onClick A unit to be executed when the button is clicked.
 * @param icon The icon to display.
 * @param iconTestTag The testTag of the icon.
 * @param contentDescription The description text of the icon.
 * @param modifier Modifier to be applied to the button. This should be avoided.
 */
@Composable
fun BasicButton(
    onClick: () -> Unit,
    icon: ImageVector,
    iconTestTag: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
  Box(
      modifier =
          modifier
              .size(30.dp)
              .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(30))
              .border(2.dp, MaterialTheme.colorScheme.background, shape = RoundedCornerShape(30))
              .clickable { onClick() },
      contentAlignment = Alignment.Center) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(30.dp).testTag(iconTestTag))
      }
}
/**
 * An outlined button with text.
 *
 * @param onClick A unit to be executed when the button is clicked.
 * @param testTag The test tag of the button.
 * @param text The text to be displayed inside the button.
 * @param backgroundColor The background color of the button.
 * @param textColor The text color of the button.
 * @param enabled The state of the button. If false, the button is disabled.
 * @param modifier Modifier to be applied to the button. This should be avoided.
 */
@Composable
fun TextButton(
    onClick: () -> Unit,
    testTag: String,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
  Button(
      onClick = onClick,
      colors = ButtonDefaults.buttonColors(backgroundColor),
      enabled = enabled,
      modifier = modifier.fillMaxWidth().height(40.dp).testTag(testTag),
  ) {
    Text(
        text,
        fontWeight = FontWeight.Bold,
        color = textColor,
        fontSize = 20.sp,
        textAlign = TextAlign.Center)
  }
}
/**
 * A square button with an icon and text.
 *
 * @param iconId The icon for the button.
 * @param onClick A unit to be executed when the button is clicked.
 * @param text The text for the button.
 * @param size The size of the button. It will be used to scale the other elements of the button.
 * @param modifier Modifier to be applied to the button. This should be avoided.
 */
@Composable
fun SquareButton(
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    text: String,
    size: Int,
    modifier: Modifier = Modifier,
) {
  Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier =
          modifier
              .size(size.dp)
              .border(2.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
              .padding(16.dp)
              .clickable { onClick() }) {
        Text(
            text = text,
            fontSize = (size * 0.15).sp,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(contentAlignment = Alignment.Center) {
          Icon(
              painter = painterResource(id = iconId),
              contentDescription = text,
              tint = MaterialTheme.colorScheme.onPrimary,
              modifier = Modifier.size((size * 0.7).dp))
        }
      }
}
