import android.content.Context
import android.content.res.AssetManager
import com.github.se.signify.model.quest.Quest
import com.github.se.signify.model.quest.QuestRepositoryLocal
import java.io.ByteArrayInputStream
import java.io.IOException
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class QuestRepositoryLocalTest {

  private val context = mock(Context::class.java)
  private val assetManager = mock(AssetManager::class.java)
  private val repository = QuestRepositoryLocal(context)

  @Test
  fun getQuests_should_return_list_of_quests_on_success() {
    // Arrange
    val fakeJson =
        """
            [
                { "index": "1", "title": "Quest 1", "description": "Des", "videoPath": "" },
                { "index": "2", "title": "Quest 2", "description": "Des", "videoPath": "" }
            ]
        """
    val inputStream = ByteArrayInputStream(fakeJson.toByteArray())
    `when`(context.assets).thenReturn(assetManager)
    `when`(assetManager.open("quests.json")).thenReturn(inputStream)

    var result: List<Quest>? = null
    var error: Exception? = null

    // Act
    repository.getQuests(
        onSuccess = { quests -> result = quests }, onFailure = { exception -> error = exception })

    // Assert
    assertNotNull(result)
    assertEquals(2, result?.size)
    assertEquals("Quest 1", result?.get(0)?.title)
    assertNull(error)
  }

  @Test
  fun `getQuests should call onFailure when an exception is thrown`() {
    // Arrange
    `when`(context.assets).thenReturn(assetManager)
    `when`(assetManager.open("quests.json")).thenThrow(IOException("File not found"))

    var result: List<Quest>? = null
    var error: Exception? = null

    // Act
    repository.getQuests(
        onSuccess = { quests -> result = quests }, onFailure = { exception -> error = exception })

    // Assert
    assertNull(result)
    assertNotNull(error)
    assertTrue(error is IOException)
    assertEquals("File not found", error?.message)
  }

  @Test
  fun `getQuests should call onFailure for invalid JSON`() {
    // Arrange
    val invalidJson = "{ invalid json }"
    val inputStream = ByteArrayInputStream(invalidJson.toByteArray())
    `when`(context.assets).thenReturn(assetManager)
    `when`(assetManager.open("quests.json")).thenReturn(inputStream)

    var result: List<Quest>? = null
    var error: Exception? = null

    // Act
    repository.getQuests(
        onSuccess = { quests -> result = quests }, onFailure = { exception -> error = exception })

    // Assert
    assertNull(result)
    assertNotNull(error)
    assertTrue(error is com.google.gson.JsonSyntaxException)
  }
}
