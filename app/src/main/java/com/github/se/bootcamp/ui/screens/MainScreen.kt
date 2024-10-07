package com.github.se.bootcamp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker.HandLandmarkerOptions
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

@Composable
fun MainAimScreen() {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    // Checking camera permission
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
        PackageManager.PERMISSION_GRANTED) {
        permissionGranted = true
    } else {
        // Request camera permission
        val permissionLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted
                ->
                permissionGranted = isGranted
                if (!isGranted) {
                    Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        LaunchedEffect(Unit) { permissionLauncher.launch(Manifest.permission.CAMERA) }
    }

    if (permissionGranted) {
        CameraPreviewView()
        LandMarkView()
    }
}

@Composable
fun CameraPreviewView() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()

                // Select back camera as default
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                // Setup preview use case
                val preview =
                    Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }

                try {
                    // Bind camera lifecycle to the preview
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            ContextCompat.getMainExecutor(context))
    }
}

@Composable
fun LandMarkView() {
    val context = LocalContext.current
    val baseOptionsBuilder = BaseOptions.builder().setModelAssetPath("hand_landmarker.task")
    val baseOptions = baseOptionsBuilder.build()

    val handLandmarker = remember {
        val optionsBuilder =
            HandLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setMinHandDetectionConfidence(0.5f)
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setResultListener { result, mpImage -> returnLivestreamResult(result, mpImage) }
                .setErrorListener { error -> returnLivestreamError(error) }

        val options = optionsBuilder.build()
        HandLandmarker.createFromOptions(context, options)
    }

    CameraPreviewWithAnalysisView(handLandmarker)
}

@Composable
fun CameraPreviewWithAnalysisView(handLandmarker: HandLandmarker) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                val preview =
                    Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }

                val imageAnalysis =
                    ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                    processImageProxyThrottled(handLandmarker, imageProxy)
                }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            ContextCompat.getMainExecutor(context))
    }
}

fun processImageProxy(handLandmarker: HandLandmarker, imageProxy: ImageProxy) {
    try {
        val bitmap = imageProxy.toBitmap()

        if (bitmap != null) {
            val frameTime = SystemClock.uptimeMillis()
            val mpImage = BitmapImageBuilder(bitmap).build()
            handLandmarker.detectAsync(mpImage, frameTime)
        } else {
            Log.e("HandLandmarker", "Bitmap conversion failed")
        }
    } catch (e: Exception) {
        Log.e("HandLandmarker", "Error processing image proxy", e)
    } finally {
        imageProxy.close()
    }
}

var lastProcessedTime = 0L

fun processImageProxyThrottled(handLandmarker: HandLandmarker, imageProxy: ImageProxy) {
    val currentTime = SystemClock.uptimeMillis()
    if (currentTime - lastProcessedTime >= 500) {
        processImageProxy(handLandmarker, imageProxy)
        lastProcessedTime = currentTime
    } else {
        imageProxy.close()
    }
}

fun ImageProxy.toBitmap(): Bitmap? {
    return try {
        val yBuffer = planes[0].buffer // Y
        val uBuffer = planes[1].buffer // U
        val vBuffer = planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
        val imageBytes = out.toByteArray()
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (e: Exception) {
        Log.e("ImageProxyToBitmap", "Failed to convert ImageProxy to Bitmap", e)
        null
    }
}

fun returnLivestreamError(runtimeException: RuntimeException?) {
    runtimeException?.printStackTrace()
}

fun returnLivestreamResult(handLandmarkerResult: HandLandmarkerResult?, mpImage: MPImage?) {

    if (handLandmarkerResult == null) {
        Log.d("HandLandmarkerResult", "No hands detected.")
        return
    }

    val data = mutableListOf<List<Float>>()

    for (hand in handLandmarkerResult.landmarks()) {
        val dataAux = mutableListOf<Float>()

        for (landmark in hand) {
            val x = landmark.x()
            val y = landmark.y()

            dataAux.add(x)
            dataAux.add(y)

            Log.d("HandLandmarkerResult", "x: $x, y: $y")
        }

        data.add(dataAux)
    }
    Log.d("HandLandmarkerResult", "Hand landmark data: $data")
}
