package dev.snvdr.heartrate.features.homepage.hp_2.camera.utils

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class LuminosityAnalyzer(private val listener: (luma: Double) -> Unit) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val buffer = imageProxy.planes[0].buffer
        val data = ByteArray(buffer.remaining())
        buffer.get(data)
        val luma = data.map { it.toInt() and 0xFF }.average()
        listener(luma)
        imageProxy.close()
    }
}