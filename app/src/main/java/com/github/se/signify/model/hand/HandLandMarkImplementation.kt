package com.github.se.signify.model.hand

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker.HandLandmarkerOptions
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import java.io.File
import java.nio.FloatBuffer
import java.util.Collections
import java.util.concurrent.Executors

class HandLandMarkImplementation(private val pathToTask: String, private val pathToModel: String): HandLandMarkRepository {

    private lateinit var handLandmarker: HandLandmarker
    private var handLandMarkerResult: HandLandmarkerResult? = null
    private lateinit var RFC_model: String
    private lateinit var session : OrtSession
    private var lastProcessedTime = 0L
    private var solution = ""
    override fun init(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath(pathToTask)
                .build()

            // Create HandLandmarker options
            val options = HandLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setMinHandDetectionConfidence(0.5f)
                .setNumHands(1)
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setResultListener { result, _ ->
                    handLandMarkerResult = result
                    returnLivestreamResult(result, context)
                }
                .setErrorListener { returnLivestreamError(it) }
                .build()

            // Initialize the HandLandmarker
            handLandmarker = HandLandmarker.createFromOptions(context, options)

             RFC_model = context.assets.open(pathToModel).use { inputStream ->
                val tempFile = File(context.cacheDir, pathToModel)
                inputStream.copyTo(tempFile.outputStream())
                tempFile.absolutePath
            }
            val env = OrtEnvironment.getEnvironment()
            session = env.createSession(RFC_model)

            onSuccess()
            /**
             * Initialize the model using pathToModel
             */

        } catch (e: Exception) {
            onFailure(e)
        }
    }

    override fun getHandLandMarkerResult(
        onSuccess: (result: HandLandmarkerResult) -> Unit
    ) {
        onSuccess(handLandMarkerResult!!)
    }

    override fun processImageProxy(
        imageProxy: ImageProxy,
        onSuccess: (result: HandLandmarkerResult) -> Unit,
        onFailure: (e: Exception) -> Unit
    ) {
        try {
            val bitmap = imageProxy.toBitmap()

            if (bitmap != null) {
                val frameTime = SystemClock.uptimeMillis()
                val mpImage = BitmapImageBuilder(bitmap).build()

                // Offload detection and inference to a background thread
                Executors.newSingleThreadExecutor().execute {
                    handLandmarker.detectAsync(mpImage, frameTime)

                    if (handLandMarkerResult != null && handLandMarkerResult?.landmarks()?.size != 0) {
                        onSuccess(handLandMarkerResult!!)
                    }
                }
            } else {
                Log.e("HandLandmarker", "Bitmap conversion failed")
            }
        } catch (e: Exception) {
            onFailure(e)
            Log.e("HandLandmarker", "Error processing image proxy", e)
        } finally {
            imageProxy.close()
        }
    }
    private fun returnLivestreamError(runtimeException: RuntimeException?) {
        runtimeException?.printStackTrace()
    }

    private fun returnLivestreamResult(
        handLandmarkerResult: HandLandmarkerResult?,
        context: Context
    ) {
        if (handLandmarkerResult == null || handLandmarkerResult.landmarks().isEmpty()) {
            return
        }
        val landmarks = handLandmarkerResult.landmarks()[0]

        val dataFrame = mutableListOf<Float>()

        for (landmark in landmarks) {
            val rotatedX = landmark.y()
            val rotatedY = landmark.x()
            dataFrame.add(rotatedX)
            dataFrame.add(rotatedY)
        }

        while (dataFrame.size < 84) {
            dataFrame.add(0.0f)
        }
        val floatArray = dataFrame.toFloatArray()
        val tensorInput = OnnxTensor.createTensor(OrtEnvironment.getEnvironment(), FloatBuffer.wrap(floatArray), longArrayOf(1, 84))

        val result = session.run(Collections.singletonMap("float_input", tensorInput))
        val x = result[0].value as Array<*>
        solution = x.get(0).toString()
        Log.d("ModelOutput", "The letter is: ${x.get(0)}")


        result.close()
        tensorInput.close()
    }
    private fun getHandLandMarks() : MutableList<MutableList<NormalizedLandmark>>? {
        return handLandMarkerResult?.landmarks()
    }

    override fun getHandLandmarker() : HandLandmarker = handLandmarker

    override fun processImageProxyThrottled(imageProxy: ImageProxy, onSuccess: (result: HandLandmarkerResult) -> Unit, onFailure: (e: Exception) -> Unit) {
        val currentTime = SystemClock.uptimeMillis()
        if (currentTime - lastProcessedTime >= 250) {
            processImageProxy(imageProxy, onSuccess, onFailure)
            lastProcessedTime = currentTime
        } else {
            imageProxy.close()
        }
    }
    override fun getSolution() : String = solution


}
