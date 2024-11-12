package com.github.se.signify.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.BottomBar
import com.github.se.signify.ui.StreakCounter
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.UtilTextButton
import com.github.se.signify.ui.getIconResId
import com.github.se.signify.ui.getImageResId
import com.github.se.signify.ui.getLetterIconResId
import com.github.se.signify.ui.getTipResId
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Data class representing an exercise with a name and an optional navigation route.
 *
 * @property name The name of the exercise, used for display and identification.
 * @property route The navigation route associated with the exercise. Defaults to "UNKNOWN_EXERCISE"
 *   if no specific route is provided.
 */
data class Exercise(val name: String, val route: String = "UNKNOWN_EXERCISE")
/**
 * Composable function that displays the home screen with various UI elements including a list of
 * exercises, a letter dictionary, and navigation buttons. The screen uses a `LazyColumn` for
 * vertical scrolling and incorporates a floating action button to quickly scroll back to the top.
 *
 * @param navigationActions The `NavigationActions` object that handles navigation between screens.
 *
 * UI Elements:
 * - **Streak Counter and Quests Button**: A row at the top displaying the user's streak counter and
 *   a button for quests.
 * - **Camera Feedback Button**: Initiates the camera feedback screen.
 * - **Letter Dictionary**: Allows users to navigate through letters, clicking on each one to scroll
 *   to the related content.
 * - **Exercise List**: Displays a horizontal pager of exercises with navigational actions for each
 *   exercise.
 * - **Alphabet Content**: A series of items for each letter ('A' to 'Z') including a title and a
 *   `SignTipBox` with letter details.
 * - **Bottom Navigation**: A bottom bar for primary navigation across screens.
 * - **Scroll-to-Top FAB**: A floating action button that scrolls to the top of the list when
 *   pressed.
 */
@Composable
fun HomeScreen(navigationActions: NavigationActions) {
    val defaultExercises =
        listOf(
            Exercise("Easy", Screen.EXERCISE_EASY),
            Exercise("Medium", Screen.EXERCISE_HARD), // To change to Medium
            Exercise("Hard", Screen.EXERCISE_HARD),
        )

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.testTag("HomeScreen"),
        bottomBar = { BottomBar(navigationActions) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { coroutineScope.launch { scrollState.animateScrollToItem(0) } },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag("ScrollToTopButton")) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to Top")
            }
        },
        content = { padding ->
            LazyColumn(
                state = scrollState, // Attach scroll state to LazyColumn
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        StreakCounter(0, false)
                        UtilButton(
                            onClick = { navigationActions.navigateTo("Quest") },
                            buttonTestTag = "QuestsButton",
                            iconTestTag = "QuestIcon",
                            icon = Icons.Outlined.DateRange,
                            contentDescription = "Quests")
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
                item {
                    CameraFeedbackButton(onClick = { navigationActions.navigateTo(Screen.PRACTICE) })
                }
                item { Spacer(modifier = Modifier.height(32.dp)) }
                item {
                    LetterDictionary(
                        scrollState = scrollState,
                        coroutineScope = coroutineScope,
                        numbOfHeaders = integerResource(R.integer.scroll_offset))
                }
                item { Spacer(modifier = Modifier.height(32.dp)) }
                item { ExerciseList(defaultExercises, navigationActions) }
                item { Spacer(modifier = Modifier.height(32.dp)) }
                // Display letters with SignTipBox in a LazyColumn
                // Putting this in CreateDictionaryWithImages
                // would lead to undesirable behavior i.e the scroll will not work as intended
                // as each item should be displayed in an item block
                items(('A'..'Z').toList()) { letter ->
                    Text(
                        text = "Letter $letter",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
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
fun CameraFeedbackButton(onClick: () -> Unit = {}) {
    UtilTextButton(
        onClickAction = onClick,
        testTag = "CameraFeedbackButton",
        text = "Try it out",
        backgroundColor = MaterialTheme.colorScheme.primary,
    )
}
/**
 * Composable function that displays a horizontally arranged letter navigator. Users can scroll
 * through each letter in the alphabet using left and right arrow buttons, and click on a letter box
 * to scroll to the corresponding item in a vertically scrollable list.
 *
 * @param scrollState The `LazyListState` associated with the list to be scrolled. Controls the
 *   scroll position.
 * @param coroutineScope The `CoroutineScope` for launching scroll actions.
 * @param numbOfHeaders The number of headers at the top of the list, allowing for an offset when
 *   scrolling to the selected letter.
 */
@Composable
fun LetterDictionary(
    scrollState: LazyListState,
    coroutineScope: CoroutineScope,
    numbOfHeaders: Int
) {
    var currentLetterIndex by remember { mutableIntStateOf(0) }
    val letters = ('a'..'z').toList()

    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().testTag("LetterDictionary"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = {
                currentLetterIndex = (currentLetterIndex - 1 + letters.size) % letters.size
            },
            modifier = Modifier.testTag("LetterDictionaryBack")) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowBack,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Back")
        }

        val currentLetter = letters[currentLetterIndex]
        Box(
            modifier =
            Modifier.border(
                2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .clickable {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(currentLetterIndex + numbOfHeaders)
                    }
                }
                .testTag("LetterBox_${currentLetter.uppercaseChar()}")) {
            Row {
                Text(
                    text = "${currentLetter.uppercaseChar()} =",
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("LetterText_${currentLetter.uppercaseChar()}"))
                Icon(
                    painter = painterResource(id = getLetterIconResId(currentLetter)),
                    contentDescription = "Letter gesture",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier =
                    Modifier.size(32.dp).testTag("LetterIcon_${currentLetter.uppercaseChar()}"))
            }
        }

        // Forward Arrow Button
        IconButton(
            onClick = { currentLetterIndex = (currentLetterIndex + 1) % letters.size },
            modifier = Modifier.testTag("LetterDictionaryForward")) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowForward,
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "Forward")
        }
    }
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseList(exercises: List<Exercise>, navigationActions: NavigationActions) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { exercises.size })

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            beyondBoundsPageCount = exercises.size,
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
                            1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .testTag("${exercises[page].name}ExerciseBox")) {
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
                            else MaterialTheme.colorScheme.surface,
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
fun ExerciseButton(exercise: Exercise, navigationActions: NavigationActions) {
    Button(
        onClick = { navigationActions.navigateTo(exercise.route) },
        modifier =
        Modifier.aspectRatio(2f)
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .testTag("${exercise.name}ExerciseButton"),
        shape = RoundedCornerShape(8.dp),
        colors =
        ButtonDefaults.buttonColors(
            MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)) {
        Text(exercise.name, modifier = Modifier.testTag("${exercise.name}ExerciseButtonText"))
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
                modifier = Modifier.size(200.dp))

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
