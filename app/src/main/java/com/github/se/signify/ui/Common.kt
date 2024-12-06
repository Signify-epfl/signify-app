package com.github.se.signify.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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
import coil.compose.AsyncImage
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.navigation.BottomNavigationMenu
import com.github.se.signify.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.ui.navigation.NavigationActions

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
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
  OutlinedButton(
      onClick = onClick,
      border =
          ButtonDefaults.outlinedButtonBorder.copy(
              width = 2.dp, brush = SolidColor(MaterialTheme.colorScheme.background)),
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
 * @param testTag The test tag of the button.
 * @param size The size of the button. It will be used to scale the other elements of the button.
 * @param modifier Modifier to be applied to the button. This should be avoided.
 */
@Composable
fun SquareButton(
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    text: String,
    testTag: String = "",
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

/**
 * A button with an icon.
 *
 * @param onClick A unit to be executed when the button is clicked.
 * @param icon The icon to display.
 * @param buttonTestTag The testTag of the button.
 * @param iconTestTag The testTag of the icon.
 * @param contentDescription The description text of the icon.
 * @param modifier Modifier to be applied to the button. This should be avoided.
 */
@Composable
fun BasicButton(
    onClick: () -> Unit,
    icon: ImageVector,
    buttonTestTag: String,
    iconTestTag: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
  Box(
      modifier =
          Modifier.size(30.dp)
              .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(30))
              .border(2.dp, MaterialTheme.colorScheme.background, shape = RoundedCornerShape(30))
              .clickable { onClick() }
              .testTag(buttonTestTag),
      contentAlignment = Alignment.Center) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(30.dp).testTag(iconTestTag))
      }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun TopBar() {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(5.dp)
              .background(MaterialTheme.colorScheme.primary)
              .testTag("TopBar"))
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun BottomBar(navigationActions: NavigationActions) {
  BottomNavigationMenu(
      onTabSelect = { route -> navigationActions.navigateTo(route) },
      tabList = LIST_TOP_LEVEL_DESTINATION,
      selectedItem = navigationActions.currentRoute())
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun BackButton(onClick: () -> Unit) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      horizontalArrangement = Arrangement.Start) {
        IconButton(onClick = { onClick() }, modifier = Modifier.testTag("BackButton")) {
          Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "BackButton",
              tint = MaterialTheme.colorScheme.primary)
        }
      }
}

/**
 * The basic column for all screens. This is a helper function for `MainScreenScaffold()` and
 * `AnnexScreenScaffold()`.
 *
 * @param padding The default padding value of the column.
 * @param testTag The test tag of the column (test tag of the screen).
 * @param content A lambda function for the content of the column.
 */
@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun ScreenColumn(
    padding: PaddingValues,
    testTag: String,
    content: @Composable ColumnScope.() -> Unit
) {
  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(padding)
              .padding(16.dp)
              .verticalScroll(rememberScrollState())
              .background(MaterialTheme.colorScheme.background)
              .testTag(testTag),
      horizontalAlignment = Alignment.CenterHorizontally) {
        content()
      }
}

/**
 * The scaffold for all main screens.
 *
 * @param navigationActions The navigationActions of the bottom navigation menu.
 * @param testTag The test tag of the column (test tag of the screen).
 * @param helpTitle The title of the info popup.
 * @param helpText The text of the info popup.
 * @param floatingActionButton A lambda function for the floating action button.
 * @param content A lambda function for the content of the column.
 */
@Composable
fun MainScreenScaffold(
    navigationActions: NavigationActions,
    testTag: String,
    helpTitle: String,
    helpText: String,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
  var isHelpBoxVisible by remember { mutableStateOf(false) }
  Scaffold(
      floatingActionButton = { floatingActionButton() },
      topBar = { TopBar() },
      bottomBar = { BottomBar(navigationActions) },
      content = { padding ->
        ScreenColumn(
            padding,
            testTag,
        ) {
          BasicButton(
              { isHelpBoxVisible = !isHelpBoxVisible },
              Icons.Outlined.Info,
              "InfoButton",
              "InfoIcon",
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
 * The scaffold for all annex screens.
 *
 * @param navigationActions The navigationActions of the bottom navigation menu.
 * @param testTag The test tag of the column (test tag of the screen).
 * @param content A lambda function for the content of the column.
 */
@Composable
fun AnnexScreenScaffold(
    navigationActions: NavigationActions,
    testTag: String,
    content: @Composable ColumnScope.() -> Unit
) {
  Scaffold(
      topBar = { TopBar() },
      content = { padding ->
        ScreenColumn(padding, testTag) {
          BackButton { navigationActions.goBack() }
          content()
        }
      })
}

/**
 * The info popup for main screens.
 *
 * @param onDismiss A lambda function to execute when the button or outside the box or is clicked.
 * @param helpTitle The title of the info popup.
 * @param helpText The text of the info popup.
 */
@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun InfoPopup(onDismiss: () -> Unit, helpTitle: String, helpText: String) {
  Dialog(onDismissRequest = { onDismiss() }) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.background, // Background for the popup
        modifier =
            Modifier.border(
                    3.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)) // Ensure the border wraps the popup
                .testTag("InfoPopup")) {
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
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("InfoPopupTitle"))
                Spacer(modifier = Modifier.height(8.dp))

                // Body text centered under the title
                Text(
                    text = helpText,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("InfoPopupBody"))
                Spacer(modifier = Modifier.height(16.dp))

                // Close button for the popup
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("InfoPopupCloseButton")) {
                      Text(text = "Close", color = MaterialTheme.colorScheme.onPrimary)
                    }
              }
        }
  }
}

/**
 * A table of user statistics.
 *
 * Important to note that the lists must have the same size.
 *
 * @param columnTestTag The principal tag for the column.
 * @param rowTestTag The principal tag for the row of stats.
 * @param lineText The text description for the statistic to show.
 * @param lineTextTestTag The text tag of the lineText.
 * @param statsTexts The list of type of stats to display.
 * @param statsNumberList The list of number for stats to display.
 */
@Composable
fun StatisticsTable(
    lineText: String,
    statsTexts: List<String>,
    statsNumberList: List<String>,
    columnTestTag: String,
    rowTestTag: String,
    lineTextTestTag: String
) {
  // Ensure that the lists have the same size
  require(statsTexts.size == statsNumberList.size) { "The lists must have the same size." }
  // Construction of the statistic column
  Column(
      modifier = Modifier.fillMaxWidth().testTag(columnTestTag),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {
        Text(
            text = lineText,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.testTag(lineTextTestTag))
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().testTag(rowTestTag),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
              for (index in statsTexts.indices) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                      Text(
                          text = statsTexts[index],
                          fontSize = 12.sp,
                          color = MaterialTheme.colorScheme.onBackground,
                          modifier = Modifier.testTag(statsTexts[index]))
                      Spacer(modifier = Modifier.width(12.dp))
                      Row(
                          modifier =
                              Modifier.size(40.dp)
                                  .border(
                                      2.dp,
                                      MaterialTheme.colorScheme.onBackground,
                                      RoundedCornerShape(12.dp))
                                  .clip(RoundedCornerShape(12.dp))
                                  .background(MaterialTheme.colorScheme.background),
                          horizontalArrangement = Arrangement.Center,
                          verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = statsNumberList[index],
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                // textAlign = TextAlign.Center,
                                modifier = Modifier.testTag(statsNumberList[index]))
                          }
                    }
              }
            }
      }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun LetterList(learnedLetters: List<Char>) {
  val allLetters = ('A'..'Z').toList() // All capital letters from A to Z
  val scrollState = rememberScrollState()

  Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState).testTag("LettersList")) {
    allLetters.forEach { letter ->
      val isLearned = letter in learnedLetters
      Text(
          text = letter.toString(),
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color =
              if (isLearned) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.primary.copy(alpha = .5f),
          modifier = Modifier.padding(horizontal = 8.dp).testTag(letter.toString()))
    }
  }
}

/**
 * A scrollable list of letters. The letters learned by a user are highlighted.
 *
 * @param lettersLearned The list of character already learned.
 */
@Composable
fun LearnedLetterList(lettersLearned: List<Char>) {
  Text(
      text = "All letters learned",
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      color = MaterialTheme.colorScheme.onBackground,
      modifier = Modifier.testTag("AllLetterLearned"))
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
              .clip(RoundedCornerShape(8.dp))
              .padding(12.dp)
              .testTag("LettersBox")) {
        LetterList(lettersLearned)
      }
}

/**
 * A streak counter that displays a user's streak in days.
 *
 * @param days A long value for the number of days (streak).
 */
@Composable
fun StreakCounter(days: Long) {
  Row(
      modifier = Modifier.testTag("StreakCounter"),
      verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.flame),
            contentDescription = "Streak Icon",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(32.dp).testTag("FlameIcon"))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$days",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 20.sp,
            modifier = Modifier.testTag("NumberOfDays"))
      }
}

/**
 * An image of a profile picture. If the URL is null, a default icon is displayed.
 *
 * @param profilePictureUrl A string for the URL of the picture.
 */
@Composable
fun ProfilePicture(profilePictureUrl: String?) {
  if (profilePictureUrl != null) {
    AsyncImage(
        model = Uri.parse(profilePictureUrl),
        contentDescription = "Profile Picture",
        modifier =
            Modifier.size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .testTag("ProfilePicture"),
        contentScale = ContentScale.Crop) // Crop the image to fit within the bounds
  } else {
    // Default placeholder for no image
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "Default Profile Picture",
        modifier = Modifier.size(120.dp).testTag("DefaultProfilePicture"),
        tint = MaterialTheme.colorScheme.onBackground)
  }
}

/**
 * A recap of a user's account information.
 *
 * @param userId A string for the user id.
 * @param userName A string for the personalized user name.
 * @param profilePictureUrl A string for the profile picture URL.
 * @param streak The user's streak in days.
 */
@Composable
fun AccountInformation(userId: String, userName: String, profilePictureUrl: String?, streak: Long) {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("UserInfo"),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically) {
        // User Info : user id and user name
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Text(
              text = userId,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.testTag("UserId"))
          Text(
              text = userName,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.testTag("UserName"))
        }

        // Profile Picture
        ProfilePicture(profilePictureUrl)

        // Number of days
        StreakCounter(streak)
      }
}

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

/**
 * The camera preview box. This function requests camera permission and displays the camera preview
 * if permission is granted.
 *
 * @param handLandMarkViewModel The ViewModel responsible for managing hand landmark detection.
 */
@Composable
fun CameraBox(handLandMarkViewModel: HandLandMarkViewModel, testTag: String = "") {
  val context = LocalContext.current
  val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
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
            Modifier.fillMaxWidth()
                .padding(16.dp)
                .height(350.dp)
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
                .border(
                    2.dp, MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp)),
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
            Modifier.fillMaxWidth()
                .padding(16.dp)
                .height(350.dp)
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
                .border(
                    2.dp, MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center) {
          Text("Camera permission required", color = MaterialTheme.colorScheme.errorContainer)
        }
  }
}
