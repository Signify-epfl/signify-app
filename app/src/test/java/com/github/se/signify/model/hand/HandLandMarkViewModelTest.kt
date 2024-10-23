import android.content.Context
import androidx.camera.core.ImageProxy
import com.github.se.signify.model.hand.HandLandMarkRepository
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argumentCaptor

@OptIn(ExperimentalCoroutinesApi::class)
class HandLandMarkViewModelTest {

  private val testDispatcher = UnconfinedTestDispatcher()
  @Mock private lateinit var mockRepository: HandLandMarkRepository

  @Mock private lateinit var mockContext: Context

  @Mock private lateinit var mockImageProxy: ImageProxy

  @Mock private lateinit var mockHandLandmarkerResult: HandLandmarkerResult

  private lateinit var viewModel: HandLandMarkViewModel

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)
    mockRepository = mock(HandLandMarkRepository::class.java)
    mockContext = mock(Context::class.java)
    mockHandLandmarkerResult = mock(HandLandmarkerResult::class.java)
    mockImageProxy = mock(ImageProxy::class.java)
    viewModel = HandLandMarkViewModel(mockRepository, mockContext)
  }

  @Test
  fun `repository init is called on ViewModel init`() {
    val contextCaptor = argumentCaptor<Context>()
    val onSuccessCaptor = argumentCaptor<() -> Unit>()
    val onFailureCaptor = argumentCaptor<(Exception) -> Unit>()

    verify(mockRepository)
        .init(contextCaptor.capture(), onSuccessCaptor.capture(), onFailureCaptor.capture())

    assertNotNull(contextCaptor.firstValue)
    onSuccessCaptor.firstValue.invoke()
  }

  @Test
  fun `processImageProxyThrottled updates landmarks when successful`() = runTest {
    val mockLandmarks = listOf(listOf(mock(NormalizedLandmark::class.java)))

    `when`(mockHandLandmarkerResult.landmarks()).thenReturn(mockLandmarks)

    doAnswer { invocation ->
          val onSuccess = invocation.getArgument<(HandLandmarkerResult) -> Unit>(1)
          onSuccess(mockHandLandmarkerResult)
          null
        }
        .`when`(mockRepository)
        .processImageProxyThrottled(anyOrNull(), any(), any())

    viewModel.processImageProxyThrottled(mockImageProxy)

    advanceUntilIdle()

    val landmarksFlow = viewModel.landMarks().first()

    assertEquals(mockLandmarks[0], landmarksFlow)
  }

  @Test
  fun `getSolution returns correct gesture output`() {
    // Mock gesture output
    `when`(mockRepository.gestureOutput()).thenReturn("A")

    val solution = viewModel.getSolution()
    assertEquals("A", solution)

    verify(mockRepository).gestureOutput()
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }
}