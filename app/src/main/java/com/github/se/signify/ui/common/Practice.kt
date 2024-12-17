package com.github.se.signify.ui.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.model.common.getIconResId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Composable function that displays a horizontally arranged letter navigator. Users can scroll
 * through each letter in the alphabet using left and right arrow buttons, and click on a letter box
 * to scroll to the corresponding item in a vertically scrollable list.
 *
 * @param coroutineScope The `CoroutineScope` for launching scroll actions.
 * @param numbOfHeaders The number of headers at the top of the list, allowing for an offset when
 *   scrolling to the selected letter.
 */
@Composable
fun LetterDictionary(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    numbOfHeaders: Int,
    clickable: Boolean = false,
    onClick: (Int, Int) -> Unit = { _, _ -> },
) {
  val letters = ('a'..'z').toList()
  val pagerState = rememberPagerState(initialPage = 0, pageCount = { letters.size })

  Box(modifier = modifier.fillMaxWidth().wrapContentHeight().testTag("LetterDictionary")) {
    // Back Arrow at the start
    IconButton(
        onClick = {
          coroutineScope.launch {
            pagerState.animateScrollToPage(
                (pagerState.currentPage - 1 + letters.size) % letters.size)
          }
        },
        modifier = Modifier.align(Alignment.CenterStart).testTag("LetterDictionaryBack")) {
          Icon(
              Icons.AutoMirrored.Outlined.ArrowBack,
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = "Back")
        }

    // Horizontal Pager for letters in the center
    HorizontalPager(
        beyondViewportPageCount = 1,
        state = pagerState,
        modifier = Modifier.size(300.dp, 50.dp).align(Alignment.Center).testTag("LetterPager"),
    ) { page ->
      val currentLetter = letters[page]
      Log.d("LetterDictionary", "Current Letter: $currentLetter")
      Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier.fillMaxSize().size(100.dp, 50.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier.border(
                            2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .then(
                            if (clickable) Modifier.clickable { onClick(page, numbOfHeaders) }
                            else Modifier)
                        // .clickable { onClick(page, numbOfHeaders) }
                        .testTag("LetterBox_${currentLetter.uppercaseChar()}")) {
                  Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = "${currentLetter.uppercaseChar()} =",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 32.sp,
                            modifier =
                                Modifier.testTag("LetterText_${currentLetter.uppercaseChar()}"))
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = getIconResId(currentLetter)),
                            contentDescription = "Letter gesture",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier =
                                Modifier.size(32.dp)
                                    .testTag("LetterIcon_${currentLetter.uppercaseChar()}"))
                      }
                }
          }
    }

    // Forward Arrow at the end
    IconButton(
        onClick = {
          coroutineScope.launch {
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % letters.size)
          }
        },
        modifier = Modifier.align(Alignment.CenterEnd).testTag("LetterDictionaryForward")) {
          Icon(
              Icons.AutoMirrored.Outlined.ArrowForward,
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = "Forward")
        }
  }
}
