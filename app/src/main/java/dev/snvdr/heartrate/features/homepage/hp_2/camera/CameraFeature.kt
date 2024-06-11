package dev.snvdr.heartrate.features.homepage.hp_2.camera

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import dev.snvdr.heartrate.features.homepage.hp_2.camera.utils.LuminosityAnalyzer
import kotlinx.coroutines.delay

@Composable
fun CameraFeature(
    modifier: Modifier = Modifier,
    isCameraCovered: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val cameraController = remember { LifecycleCameraController(context) }
    var cameraCoveredState by remember { mutableStateOf(false) }

    AndroidView(
        modifier = modifier,
        factory = {
            createPreviewView(
                context,
                lifecycleOwner,
                cameraController,
                isCameraCovered = { cameraCoveredState = it }
            )
        }
    )

    LaunchedEffect(cameraCoveredState) {
        delay(250)
        isCameraCovered(cameraCoveredState)
    }

}

private fun createPreviewView(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraController: LifecycleCameraController,
    isCameraCovered: (Boolean) -> Unit,
): PreviewView {
    val previewView = PreviewView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setBackgroundColor(Color.BLACK)
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }

    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        bindCamera(
            cameraProvider,
            lifecycleOwner,
            previewView,
            isCameraCovered = { isCameraCovered(it) })
    }, ContextCompat.getMainExecutor(context))

    previewView.controller = cameraController
    cameraController.bindToLifecycle(lifecycleOwner)

    return previewView
}

private fun bindCamera(
    cameraProvider: ProcessCameraProvider,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    isCameraCovered: (Boolean) -> Unit,
) {
    val preview = Preview.Builder().build().also {
        it.setSurfaceProvider(previewView.surfaceProvider)
    }

    val imageAnalyzer = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .also {
            it.setAnalyzer(
                ContextCompat.getMainExecutor(previewView.context),
                LuminosityAnalyzer { luma -> isCameraCovered(luma < LUMINOSITY_THRESHOLD) }
            )
        }

    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(
        lifecycleOwner,
        CameraSelector.DEFAULT_BACK_CAMERA,
        preview,
        imageAnalyzer
    )
}

private const val LUMINOSITY_THRESHOLD = 50.0