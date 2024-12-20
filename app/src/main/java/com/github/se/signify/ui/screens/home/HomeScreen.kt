package com.github.se.signify.ui.screens.home

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.model.common.getIconResId
import com.github.se.signify.model.common.getImageResId
import com.github.se.signify.model.common.getTipResId
import com.github.se.signify.model.home.exercise.ExerciseLevel
import com.github.se.signify.model.home.exercise.ExerciseLevelName
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.navigation.TopLevelDestinations
import com.github.se.signify.ui.common.BasicButton
import com.github.se.signify.ui.common.HelpText
import com.github.se.signify.ui.common.LetterDictionary
import com.github.se.signify.ui.common.MainScreenScaffold
import com.github.se.signify.ui.common.TextButton
import com.github.se.signify.ui.screens.tutorial.LocalElementPositions
import com.github.se.signify.ui.screens.tutorial.saveElementPosition
import kotlinx.coroutines.launch

/**
 * Composable function that displays the home screen with various UI elements including a list of
 * exercises, a letter dictionary, and navigation buttons. The screen uses a `LazyColumn` for
 * vertical scrolling and incorporates a floating action button to quickly scroll back to the top.
 *
 * @param navigationActions The `NavigationActions` object that handles navigation between screens.
 */
@Composable
fun HomeScreen(navigationActions: NavigationActions) {
  val defaultExercises = ExerciseLevel.entries
  val elementPositions = LocalElementPositions.current
  val density = LocalDensity.current

  val scrollState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  val letterText = stringResource(id = R.string.letter_text)

  val topBarButtons =
      listOf<@Composable () -> Unit>(
          {
            FeedbackButton(
                navigationActions,
                Modifier.onGloballyPositioned { coords ->
                  saveElementPosition("FeedbackButton", coords, density, elementPositions)
                })
          },
          {
            QuizButton(
                navigationActions,
                Modifier.onGloballyPositioned { coords ->
                  saveElementPosition("QuizButton", coords, density, elementPositions)
                })
          },
          {
            QuestsButton(
                navigationActions,
                Modifier.onGloballyPositioned { coords ->
                  saveElementPosition("QuestsButton", coords, density, elementPositions)
                })
          })
  var numOfHeaders = 0
  // Define other headers dynamically
  val additionalHeaders =
      listOf<@Composable () -> Unit>(
          { Spacer(modifier = Modifier.height(32.dp)) },
          {
            CameraFeedbackButton(
                onClick = { navigationActions.navigateTo(Screen.PRACTICE) },
                modifier =
                    Modifier.onGloballyPositioned { coords ->
                      saveElementPosition("CameraFeedbackButton", coords, density, elementPositions)
                    })
          },
          {
            LetterDictionary(
                coroutineScope = coroutineScope,
                numbOfHeaders = numOfHeaders, // Dynamically computed later
                clickable = true,
                onClick = { page, numbOfHeaders ->
                  coroutineScope.launch { scrollState.scrollToItem(page + numbOfHeaders) }
                },
                modifier =
                    Modifier.onGloballyPositioned { coords ->
                      saveElementPosition("LetterDictionary", coords, density, elementPositions)
                    })
          })

  // Calculate headers dynamically
  numOfHeaders = calculateNumOfHeadersOfTopBarButtons(topBarButtons, additionalHeaders)

  MainScreenScaffold(
      navigationActions = navigationActions,
      topLevelDestination = TopLevelDestinations.HOME,
      testTag = "HomeScreen",
      helpText =
          HelpText(
              title = stringResource(R.string.home_text),
              content = stringResource(R.string.help_home_screen_text)),
      topBarButtons = topBarButtons,
      floatingActionButton = {
        FloatingActionButton(
            onClick = { coroutineScope.launch { scrollState.animateScrollToItem(0) } },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.testTag("ScrollToTopButton")) {
              Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to Top")
            }
      },
      content = {
        LazyColumn(state = scrollState, modifier = Modifier.weight(1f)) {
          additionalHeaders.forEach { composable -> item { composable() } }
          item { Spacer(modifier = Modifier.height(32.dp)) }
          item {
            ExerciseList(
                defaultExercises,
                navigationActions,
                Modifier.onGloballyPositioned { coords ->
                  saveElementPosition("ExerciseList", coords, density, elementPositions)
                })
          }
          item { Spacer(modifier = Modifier.height(32.dp)) }
          items(('A'..'Z').toList()) { letter ->
            Text(
                text = "$letterText $letter",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp).testTag("LetterTextDict_$letter"))
            SignTipBox(letter = letter)
            Spacer(modifier = Modifier.height(16.dp))
          }
        }
      })
}

/**
 * Composable function that displays a button for initiating camera feedback functionality. The
 * button uses a default background color and a customizable click action.
 *
 * @param onClick The action to perform when the button is clicked. Defaults to an empty action.
 */
@Composable
fun CameraFeedbackButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
  val tryText = stringResource(id = R.string.try_text)
  TextButton(
      onClick = onClick,
      testTag = "CameraFeedbackButton",
      text = tryText,
      backgroundColor = MaterialTheme.colorScheme.primary,
      textColor = MaterialTheme.colorScheme.onPrimary,
      modifier = modifier)
}

/**
 * Composable function that displays a horizontally scrollable list of exercises using a pager. Each
 * exercise is shown with a centered button that, when clicked, navigates to the exercise's specific
 * route. Below the list, a dot indicator displays the current page position within the list.
 *
 * @param exercises The list of `Exercise` objects to be displayed. Each exercise has a name and a
 *   navigation route.
 * @param navigationActions The `NavigationActions` object responsible for handling navigation
 *   between screens.
 */
@Composable
fun ExerciseList(
    exercises: List<ExerciseLevel>,
    navigationActions: NavigationActions,
    modifier: Modifier = Modifier
) {
  val pagerState = rememberPagerState(initialPage = 0, pageCount = { exercises.size })

  Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
    HorizontalPager(
        beyondViewportPageCount = exercises.size,
        state = pagerState,
        modifier = Modifier.height(160.dp).padding(8.dp).testTag("ExerciseListPager"),
        verticalAlignment = Alignment.CenterVertically) { page ->
          Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Box(
                    modifier =
                        Modifier.size(200.dp, 100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .border(
                                1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                            .testTag("${exercises[page].id}ExerciseBox")) {
                      Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        ExerciseButton(
                            exercise = exercises[page], navigationActions = navigationActions)
                      }
                    }
              }
        }

    // Dots Indicator below the exercise box
    Row(
        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
          exercises.forEachIndexed { index, _ ->
            Box(
                modifier =
                    Modifier.size(17.dp)
                        .padding(4.dp)
                        .background(
                            color =
                                if (pagerState.currentPage == index)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primary.copy(alpha = .5f),
                            shape = RoundedCornerShape(50)))
          }
        }
  }
}

/**
 * Composable function that displays a button for an exercise. When clicked, the button triggers a
 * navigation action to the specified exercise route.
 *
 * @param exercise The `Exercise` object representing the exercise. Contains the name and navigation
 *   route.
 * @param navigationActions The `NavigationActions` object that handles navigation between screens.
 */
@Composable
fun ExerciseButton(exercise: ExerciseLevel, navigationActions: NavigationActions) {
  Button(
      onClick = { navigationActions.navigateTo(exercise.screen) },
      modifier =
          Modifier.aspectRatio(2f)
              .fillMaxWidth()
              .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
              .testTag("${exercise.id}ExerciseButton"),
      shape = RoundedCornerShape(8.dp),
      colors =
          ButtonDefaults.buttonColors(
              MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)) {
        val context = LocalContext.current
        val exerciseText = ExerciseLevelName.getLevelName(context, exercise)
        Text(exerciseText, modifier = Modifier.testTag("${exercise.id}ExerciseButtonText"))
      }
}

/**
 * Composable function that displays information about a letter in a structured box. This includes
 * an image, an icon, and a tip text related to the given letter. The layout is styled with padding
 * and background, making it suitable for display within lists or standalone UI components.
 *
 * @param letter The character representing the letter for which the image, icon, and tip are
 *   displayed.
 * @param modifier The `Modifier` to apply to this box, allowing customization of padding, size, and
 *   layout behavior.
 */
@Composable
fun SignTipBox(letter: Char, modifier: Modifier = Modifier) {
  val imageResId = getImageResId(letter)
  val iconResId = getIconResId(letter)
  val tipText = stringResource(id = getTipResId(letter))

  Box(
      modifier =
          modifier
              .padding(16.dp)
              .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
              .padding(8.dp)
              .testTag("SignTipBox_$letter")) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
              // Displaying the main image, e.g., `pic_a.jpg`
              Image(
                  painter = painterResource(id = imageResId),
                  contentDescription = "Image for letter $letter",
                  modifier =
                      Modifier.size(200.dp)
                          .border(2.dp, color = MaterialTheme.colorScheme.background))

              Spacer(modifier = Modifier.height(16.dp))
              // Displaying the description text with the icon, e.g., `letter_a.png`
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Icon for letter $letter",
                    modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tipText,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(8.dp))
              }
            }
      }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun FeedbackButton(navigationActions: NavigationActions, modifier: Modifier = Modifier) {
  BasicButton(
      onClick = { navigationActions.navigateTo(Screen.FEEDBACK) },
      iconTestTag = "FeedbackIcon",
      contentDescription = "Feedback",
      modifier = modifier.testTag("FeedbackButton"),
      icon = Icons.Outlined.Email)
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun QuizButton(navigationActions: NavigationActions, modifier: Modifier = Modifier) {
  BasicButton(
      onClick = { navigationActions.navigateTo(Screen.QUIZ) },
      iconTestTag = "QuizIcon",
      contentDescription = "Quizzes",
      modifier = modifier.testTag("QuizButton"),
      icon = Icons.Outlined.Star)
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun QuestsButton(navigationActions: NavigationActions, modifier: Modifier = Modifier) {
  BasicButton(
      onClick = { navigationActions.navigateTo(Screen.QUEST) },
      iconTestTag = "QuestIcon",
      contentDescription = "Quests",
      modifier = modifier.testTag("QuestsButton"),
      icon = Icons.Outlined.DateRange)
}
/**
 * Computes the total number of headers dynamically based on the defined structure.
 *
 * @param topBarButtons The list of top bar buttons which are considered as headers.
 * @param additionalComponents A list of additional composables or header items to count
 *   dynamically.
 * @return The total number of headers.
 */
fun calculateNumOfHeadersOfTopBarButtons(
    topBarButtons: List<@Composable () -> Unit>,
    additionalComponents: List<@Composable () -> Unit>
): Int {
  // Count headers in both topBarButtons and additionalComponents dynamically
  return topBarButtons.size + additionalComponents.size
}
