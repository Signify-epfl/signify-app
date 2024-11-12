package com.github.se.signify.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.navigation.BottomNavigationMenu
import com.github.se.signify.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.ui.navigation.NavigationActions

/**
 * A reusable composable function that creates an outlined button with an icon.
 *
 * @param onClickAction A lambda function to execute when the button is clicked.
 * @param icon The ImageVector representing the icon to be displayed in the button.
 * @param iconDescription A string description of the icon, used for accessibility and testing.
 */
@Composable
fun UtilIconButton(onClickAction: () -> Unit, icon: ImageVector, iconDescription: String) {
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
 * @param testTag A string used for testing, which serves as the tag for the button.
 * @param text The text to be displayed inside the button.
 * @param backgroundColor The background color of the button.
 */
@Composable
fun UtilTextButton(
    onClickAction: () -> Unit,
    testTag: String,
    text: String,
    backgroundColor: Color,
) {
  OutlinedButton(
      onClick = onClickAction,
      border =
          ButtonDefaults.outlinedButtonBorder.copy(
              width = 2.dp, brush = SolidColor(colorResource(R.color.black))),
      colors = ButtonDefaults.buttonColors(backgroundColor),
      modifier = Modifier.fillMaxWidth().height(40.dp).testTag(testTag)) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.black),
            fontSize = 20.sp,
            textAlign = TextAlign.Center)
      }
}

/**
 * A reusable composable function that creates a square button with customizable text above the
 * icon.
 *
 * @param iconRes The icon for the button.
 * @param label The text for the button.
 * @param onClick A lambda function to execute when the button is clicked.
 * @param size The size of the button. It will be used to scale the other elements of the button.
 */
@Composable
fun SquareButton(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    size: Int,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
  Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier =
          modifier
              .size(size.dp)
              .border(2.dp, colorResource(R.color.black), RoundedCornerShape(12.dp))
              .background(colorResource(R.color.blue), RoundedCornerShape(12.dp))
              .padding(16.dp)
              .clickable { onClick() }) {
        Text(
            text = label,
            fontSize = (size * 0.15).sp,
            color = colorResource(R.color.dark_gray),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(contentAlignment = Alignment.Center) {
          Icon(
              painter = painterResource(id = iconRes),
              contentDescription = label,
              tint = colorResource(R.color.dark_gray),
              modifier = Modifier.size((size * 0.7).dp))
        }
      }
}

/**
 * A reusable composable function that creates an icon button.
 *
 * @param onClick A lambda function to execute when the button is clicked.
 * @param buttonTestTag The testTag of the button.
 * @param iconTestTag The testTag of the icon.
 * @param icon The icon to display.
 * @param contentDescription The description text of the icon.
 */
@Composable
fun UtilButton(
    onClick: () -> Unit,
    buttonTestTag: String,
    iconTestTag: String,
    icon: ImageVector,
    contentDescription: String
) {
  Box(
      modifier =
          Modifier.size(30.dp)
              .background(colorResource(R.color.white), shape = RoundedCornerShape(30))
              .border(2.dp, colorResource(R.color.white), shape = RoundedCornerShape(30))
              .clickable { onClick() }
              .testTag(buttonTestTag),
      contentAlignment = Alignment.Center) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = colorResource(R.color.blue),
            modifier = Modifier.size(30.dp).testTag(iconTestTag))
      }
}

/** A reusable composable function that creates the blue top bar. */
@Composable
fun TopBar() {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(5.dp)
              .background(MaterialTheme.colorScheme.primary)
              .testTag("TopBlueBar"))
}

/**
 * A reusable composable function that creates the bottom navigation menu.
 *
 * @param navigationActions The navigationActions of the bottom navigation menu.
 */
@Composable
fun BottomBar(navigationActions: NavigationActions) {
  BottomNavigationMenu(
      onTabSelect = { route -> navigationActions.navigateTo(route) },
      tabList = LIST_TOP_LEVEL_DESTINATION,
      selectedItem = navigationActions.currentRoute(),
      modifier = Modifier.testTag("BottomNavigationMenu"))
}

/**
 * A reusable composable function that creates the back button.
 *
 * @param onClick A lambda function to execute when the button is clicked.
 */
@Composable
fun BackButton(onClick: () -> Unit) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      horizontalArrangement = Arrangement.Start) {
        IconButton(onClick = { onClick() }, modifier = Modifier.testTag("BackButton")) {
          Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "BackButton",
              tint = colorResource(R.color.blue))
        }
      }
}

/**
 * A reusable composable function that creates a basic column for all screens. It is a helper
 * function for ScaffoldMainScreen() and ScaffoldAnnexeScreen().
 *
 * @param padding The default padding value of the column.
 * @param testTag The test tag of the column (test tag of the screen).
 * @param backgroundColor The color of the background (has to be defined by the theme).
 * @param content A lambda function for the content of the column.
 */
@Composable
fun ScreenColumn(
    padding: PaddingValues,
    testTag: String,
    backgroundColor: Color = colorResource(R.color.white),
    content: @Composable ColumnScope.() -> Unit
) {
  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(padding)
              .padding(16.dp)
              .verticalScroll(rememberScrollState())
              .background(backgroundColor)
              .testTag(testTag),
      horizontalAlignment = Alignment.CenterHorizontally) {
        content()
      }
}

/**
 * A reusable composable function that creates a basic scaffold for all the main screens.
 *
 * @param navigationActions The navigationActions of the bottom navigation menu.
 * @param testTagColumn The test tag of the column (test tag of the screen).
 * @param helpTitle The title of the info popup.
 * @param helpText The text of the info popup.
 * @param content A lambda function for the content of the column.
 */
@Composable
fun MainScreenScaffold(
    navigationActions: NavigationActions,
    testTagColumn: String,
    helpTitle: String,
    helpText: String,
    content: @Composable ColumnScope.() -> Unit
) {
  var isHelpBoxVisible by remember { mutableStateOf(false) }
  Scaffold(
      topBar = { TopBar() },
      bottomBar = { BottomBar(navigationActions) },
      content = { padding ->
        ScreenColumn(
            padding,
            testTagColumn,
            colorResource(R.color.white) // to replace with the background color theme
            ) {
              UtilButton(
                  { isHelpBoxVisible = !isHelpBoxVisible },
                  "InfoButton",
                  "InfoIcon",
                  Icons.Outlined.Info,
                  "Help")
              content()
              // Show popup when the info button is clicked
              if (isHelpBoxVisible) {
                InfoPopup(
                    onDismiss = { isHelpBoxVisible = false },
                    helpTitle = helpTitle,
                    helpText = helpText)
              }
            }
      })
}

/**
 * A reusable composable function that creates a basic scaffold for all the annexe screens.
 *
 * @param navigationActions The navigationActions of the bottom navigation menu.
 * @param testTagColumn The test tag of the column (test tag of the screen).
 * @param content A lambda function for the content of the column.
 */
@Composable
fun AnnexScreenScaffold(
    navigationActions: NavigationActions,
    testTagColumn: String,
    content: @Composable ColumnScope.() -> Unit
) {
  Scaffold(
      topBar = { TopBar() },
      content = { padding ->
        ScreenColumn(
            padding,
            testTagColumn,
            colorResource(R.color.white) // to replace with the background color theme
            ) {
              BackButton { navigationActions.goBack() }
              content()
            }
      })
}

/**
 * A reusable composable function that creates the info popup for help.
 *
 * @param onDismiss A lambda function to execute when the button or outside the box or is clicked.
 * @param helpTitle The title of the info popup.
 * @param helpText The text of the info popup.
 */
@Composable
fun InfoPopup(onDismiss: () -> Unit, helpTitle: String, helpText: String) {
  Dialog(onDismissRequest = { onDismiss() }) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = colorResource(R.color.blue), // Blue background for the popup
        modifier =
            Modifier.border(
                    3.dp,
                    colorResource(R.color.dark_gray),
                    RoundedCornerShape(12.dp)) // Ensure the black border wraps the popup
                .padding(16.dp)
                .testTag("InfoPopup") // Remove padding for the border
        ) {
          Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth().testTag("InfoPopupContent"),
              horizontalAlignment = Alignment.CenterHorizontally) {
                // Title centered and underlined
                Text(
                    text =
                        buildAnnotatedString {
                          withStyle(
                              style =
                                  SpanStyle(
                                      fontWeight = FontWeight.Bold,
                                      textDecoration = TextDecoration.Underline)) {
                                append(helpTitle)
                              }
                        },
                    fontSize = 20.sp,
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("InfoPopupTitle"))
                Spacer(modifier = Modifier.height(8.dp))

                // Body text centered under the title
                Text(
                    text = helpText,
                    fontSize = 16.sp,
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("InfoPopupBody"))
                Spacer(modifier = Modifier.height(16.dp))

                // Close button for the popup
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_gray)),
                    modifier = Modifier.testTag("InfoPopupCloseButton")) {
                      Text(text = "Close", color = colorResource(R.color.white))
                    }
              }
        }
  }
}

/**
 * A reusable composable function that creates a row for statistics.
 *
 * Important to note that the list have the same size and the inner list should match their sizes.
 * Example (the number are the size): list1 = ((2), (1), (2)) => list2 = ((2), (1), (2)) and list3 =
 * (3)
 *
 * @param rowTestTag The principal tag for the row.
 * @param lineText The text description for the statistic to show.
 * @param columnTextList A list of list of strings for the information to show.
 * @param columnTextSPList A list of list of int that correspond to the size of each string of
 *   columnTextList, (recommend 12.sp for text and 20.sp for number).
 * @param columnTextTagList A list of textTag corresponding to the column.
 */
@Composable
fun StatisticsRow(
    rowTestTag: String,
    lineText: String,
    lineTextTag: String,
    columnTextList: List<List<String>>,
    columnTextSPList: List<List<Int>>,
    columnTextTagList: List<String>
) {
  // Ensure that the list have the same size
  require(
      columnTextList.size == columnTextSPList.size &&
          columnTextList.size == columnTextTagList.size) {
        "All lists must have the same size."
      }
  columnTextList.zip(columnTextSPList).forEachIndexed { index, (textList, spList) ->
    require(textList.size == spList.size) { "Sub lists at index $index must have the same size." }
  }
  // Construction of the statistic row
  Row(
      modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).testTag(rowTestTag),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = lineText,
            fontSize = 16.sp,
            color = colorResource(R.color.black),
            modifier = Modifier.testTag(lineTextTag))
        for (index in columnTextList.indices) {
          Column(
              modifier =
                  Modifier.size(50.dp)
                      .border(2.dp, colorResource(R.color.blue), RoundedCornerShape(12.dp))
                      .clip(RoundedCornerShape(12.dp))
                      .background(colorResource(R.color.white))
                      .testTag(columnTextTagList[index]),
              horizontalAlignment = Alignment.CenterHorizontally) {
                for (subIndex in columnTextList[index].indices) {
                  Text(
                      text = columnTextList[index][subIndex],
                      fontSize = columnTextSPList[index][subIndex].sp,
                      color = colorResource(R.color.black),
                      modifier = Modifier.testTag(columnTextList[index][subIndex]))
                }
              }
        }
      }
}

/**
 * A reusable composable function that creates the scrollable letter list. The letter already
 * learned are in blue and the others are in gray.
 *
 * Important to note that it is helper function to AllLetterLearned()
 *
 * @param lettersLearned The list of character already learned.
 */
@Composable
fun HorizontalLetterList(lettersLearned: List<Char>) {
  val allLetters = ('A'..'Z').toList() // All capital letters from A to Z
  val scrollState = rememberScrollState()

  Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState).testTag("LettersList")) {
    allLetters.forEach { letter ->
      val isLearned = letter in lettersLearned
      Text(
          text = letter.toString(),
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color =
              if (isLearned) colorResource(R.color.blue)
              else colorResource(R.color.dark_gray).copy(alpha = .5f),
          modifier = Modifier.padding(horizontal = 8.dp).testTag(letter.toString()))
    }
  }
}

/**
 * A reusable composable function that creates the text and the scrollable letter list.
 *
 * @param lettersLearned The list of character already learned.
 */
@Composable
fun LearnedLetterList(lettersLearned: List<Char>) {
  Text(
      text = "All letters learned",
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      color = colorResource(R.color.dark_gray),
      modifier = Modifier.testTag("AllLetterLearned"))
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .border(2.dp, colorResource(R.color.dark_gray), RoundedCornerShape(12.dp))
              .clip(RoundedCornerShape(8.dp))
              .padding(12.dp)
              .testTag("LettersBox")) {
        HorizontalLetterList(lettersLearned)
      }
}

/**
 * A reusable composable function that creates the streak counter.
 *
 * @param days An int value for the number of days (streak).
 * @param daysText A boolean value => true will display the string " days" after the number.
 */
@Composable
fun StreakCounter(days: Int, daysText: Boolean) {
  val text = if (daysText) " days" else ""
  Row(
      modifier = Modifier.testTag("StreakCounter"),
      verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.flame),
            contentDescription = "Streak Icon",
            tint = colorResource(R.color.red),
            modifier = Modifier.size(32.dp).testTag("FlameIcon"))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$days$text",
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.red),
            fontSize = 20.sp,
            modifier = Modifier.testTag("NumberOfDays"))
      }
}

/**
 * A reusable composable function that creates the profile picture.
 *
 * @param profilePictureUrl A string for the URL of the picture.
 */
@Composable
fun ProfilePicture(profilePictureUrl: String?) {
  Box(
      modifier =
          Modifier.size(80.dp)
              .clip(CircleShape)
              .background(colorResource(R.color.dark_gray))
              .testTag("ProfilePicture")) {
        profilePictureUrl?.let {
          // Load image with Coil or any other image loading library
          Image(
              painter = rememberAsyncImagePainter(model = it),
              contentDescription = "Profile picture",
              modifier = Modifier.fillMaxSize())
        }
            ?: Text(
                text = "Profile",
                modifier = Modifier.align(Alignment.Center),
                color = colorResource(R.color.white))
      }
}

/**
 * A reusable composable function that creates the profile information.
 *
 * @param userId A string for the user id.
 * @param userName A string for the personalized user name.
 * @param profilePictureUrl A string for the profile picture URL.
 * @param days An int value for the number of days (streak).
 */
@Composable
fun AccountInformation(userId: String, userName: String, profilePictureUrl: String?, days: Int) {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("UserInfo"),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically) {
        // User Info : user id and user name
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Text(
              text = userId,
              fontWeight = FontWeight.Bold,
              color = colorResource(R.color.dark_gray),
              modifier = Modifier.testTag("UserId"))
          Text(
              text = userName,
              fontWeight = FontWeight.Bold,
              color = colorResource(R.color.dark_gray),
              modifier = Modifier.testTag("UserName"))
        }
        Spacer(modifier = Modifier.width(24.dp))

        // Profile Picture
        ProfilePicture(profilePictureUrl)
        Spacer(modifier = Modifier.width(24.dp))

        // Number of days
        StreakCounter(days, true)
      }
}

/**
 * A reusable composable function that creates an outlined button with customizable text.
 *
 * @param testTag A string for the test tag.
 * @param text The text to be displayed inside the box.
 */
@Composable
fun NotImplementedYet(testTag: String, text: String) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(240.dp)
              .border(3.dp, colorResource(R.color.blue), RoundedCornerShape(12.dp))
              .background(colorResource(R.color.black), RoundedCornerShape(12.dp))
              .padding(16.dp)
              .testTag(testTag),
      contentAlignment = Alignment.Center) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = colorResource(R.color.white),
            fontWeight = FontWeight.Normal)
      }
}

/**
 * A function that returns the corresponding icon for the letter.
 *
 * @param letter The character from which we want the icon.
 */
fun getLetterIconResId(letter: Char): Int {
  return when (letter) {
    'a' -> R.drawable.letter_a
    'b' -> R.drawable.letter_b
    'c' -> R.drawable.letter_c
    'd' -> R.drawable.letter_d
    'e' -> R.drawable.letter_e
    'f' -> R.drawable.letter_f
    'g' -> R.drawable.letter_g
    'h' -> R.drawable.letter_h
    'i' -> R.drawable.letter_i
    'j' -> R.drawable.letter_j
    'k' -> R.drawable.letter_k
    'l' -> R.drawable.letter_l
    'm' -> R.drawable.letter_m
    'n' -> R.drawable.letter_n
    'o' -> R.drawable.letter_o
    'p' -> R.drawable.letter_p
    'q' -> R.drawable.letter_q
    'r' -> R.drawable.letter_r
    's' -> R.drawable.letter_s
    't' -> R.drawable.letter_t
    'u' -> R.drawable.letter_u
    'v' -> R.drawable.letter_v
    'w' -> R.drawable.letter_w
    'x' -> R.drawable.letter_x
    'y' -> R.drawable.letter_y
    'z' -> R.drawable.letter_z
    else -> R.drawable.letter_a // Default case, just in case an unexpected value is passed
  }
}
/**
 * Composable function that serves as a placeholder for the camera preview. This function requests
 * camera permission and displays the camera preview if permission is granted.
 *
 * @param handLandMarkViewModel The ViewModel responsible for managing hand landmark detection.
 * @param modifier Modifier to be applied to the camera placeholder.
 */
@Composable
fun CameraPlaceholder(handLandMarkViewModel: HandLandMarkViewModel, modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val previewView = remember { PreviewView(context) }
  var permissionGranted by remember { mutableStateOf(false) }

  // Permission launcher to request camera permission
  val permissionLauncher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.RequestPermission(),
          onResult = { isGranted ->
            permissionGranted = isGranted
            if (!isGranted) {
              Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
          })

  // Check for permission on load
  LaunchedEffect(Unit) {
    permissionGranted =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
    if (!permissionGranted) {
      permissionLauncher.launch(Manifest.permission.CAMERA)
    }
  }

  if (permissionGranted) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(350.dp)
                .background(colorResource(R.color.blue), shape = RoundedCornerShape(16.dp))
                .border(2.dp, colorResource(R.color.white), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center) {
          AndroidView(
              factory = { previewView },
              modifier =
                  Modifier.fillMaxWidth()
                      .fillMaxHeight()
                      .clip(RoundedCornerShape(16.dp))
                      .testTag("cameraPreview"))

          // Set up the camera preview
          LaunchedEffect(Unit) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener(
                {
                  val cameraProvider = cameraProviderFuture.get()
                  val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                  val preview =
                      Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                      }
                  val imageAnalysis =
                      ImageAnalysis.Builder()
                          .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                          .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                          .build()
                  imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    handLandMarkViewModel.processImageProxyThrottled(imageProxy)
                  }

                  try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageAnalysis)
                  } catch (e: Exception) {
                    Log.e("CameraPlaceholder", "Camera binding failed", e)
                  }
                },
                ContextCompat.getMainExecutor(context))
          }
        }
  } else {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(350.dp)
                .background(colorResource(R.color.black), shape = RoundedCornerShape(16.dp))
                .border(2.dp, colorResource(R.color.white), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center) {
          Text("Camera permission required", color = colorResource(R.color.white))
        }
  }
}
