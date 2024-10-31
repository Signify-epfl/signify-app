import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.github.se.signify.model.quest.Quest
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.quest.QuestViewModel
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Route
import com.github.se.signify.ui.screens.home.QuestBox
import com.github.se.signify.ui.screens.home.QuestScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class QuestScreenTest {

  private lateinit var questRepository: QuestRepository
  private lateinit var navigationActions: NavigationActions
  private lateinit var questViewModel: QuestViewModel

  val sampleQuest =
      Quest(index = "1", title = "Sample Quest", description = "This is a sample quest description")

  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun setUp() {
    questRepository = mock(QuestRepository::class.java)
    navigationActions = mock(NavigationActions::class.java)
    questViewModel = QuestViewModel(questRepository)

    `when`(navigationActions.currentRoute()).thenReturn(Route.QUEST)
  }

  @Test
  fun hasRequiredComponent() {
    composeTestRule.setContent {
      QuestScreen(navigationActions = navigationActions, questViewModel = questViewModel)
    }

    composeTestRule.onNodeWithTag("QuestScreen").assertIsDisplayed()
  }

  @Test
  fun questBoxDisplaysCorrectInformation() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest) }

    composeTestRule.onNodeWithTag("QuestCard").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestHeader").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestDescription").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestActionButton").assertIsDisplayed()
  }

  @Test
  fun questActionButtonHasClickAction() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest) }

    composeTestRule.onNodeWithTag("QuestActionButton").assertHasClickAction()
  }
}
