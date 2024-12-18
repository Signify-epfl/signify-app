package com.github.se.signify.ui.screens.tutorial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.common.TextButton

@Composable
fun TutorialOverlay(
    text: String,
    highlightArea: HighlightedArea,
    onNext: () -> Unit,
    textTag: String
) {
  Box(modifier = Modifier.fillMaxSize().testTag("HighlightArea")) {

    // Highlight the relevant area
    HighlightArea(highlightArea)

    // Tutorial message and button
    Column(
        modifier =
            Modifier.align(Alignment.BottomCenter)
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
          text = text,
          color = MaterialTheme.colorScheme.onPrimary,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(bottom = 16.dp).testTag(textTag))
      TextButton(
          onClick = onNext,
          testTag = "nextButton",
          text = stringResource(R.string.next_text),
          backgroundColor = MaterialTheme.colorScheme.primary,
          textColor = MaterialTheme.colorScheme.onPrimary,
      )
    }
  }
}
