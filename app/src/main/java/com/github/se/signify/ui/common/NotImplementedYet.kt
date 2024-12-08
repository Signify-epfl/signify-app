package com.github.se.signify.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A placeholder box for a feature that has not been implemented yet.
 *
 * @param testTag A string for the test tag.
 * @param text The text to be displayed inside the box.
 */
@Composable
fun NotImplementedYet(text: String, testTag: String) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(240.dp)
              .border(3.dp, MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(12.dp))
              .padding(16.dp)
              .testTag(testTag),
      contentAlignment = Alignment.Center) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onErrorContainer,
            fontWeight = FontWeight.Normal)
      }
}
