package com.limonia.tflite

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.math.min

class ModelTFLite(assetManager: AssetManager, modelPath: String, labelPath: String, private val inputSize: Int) {
    private var interpreter: Interpreter
    private var labelList: List<String>
    private val pixelSize: Int = 3
    private val imageMean = 0
    private val imageStd = 255f
    private val maxResult = 3
    private val threshHold = 0.4f

    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0f) {
        override fun toString(): String {
            return "Title = $title, Confidence = $confidence"
        }
    }

    init {
        val options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)
        interpreter = Interpreter(loadModelFile(assetManager, modelPath),options)
        labelList = loadLabelList(assetManager, labelPath)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String) : MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }

    /*Devuelve el resultado después de ejecutar el reconocimiento con la ayuda
     del intérprete en el mapa de bits pasado.*/
    fun recognizeImage(bitmap: Bitmap): List<Recognition> {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        val result = Array(1) { FloatArray(labelList.size) }
        interpreter.run(byteBuffer, result)
        return getSortedResult(result)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * pixelSize)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValue = IntArray(inputSize * inputSize)

        bitmap.getPixels(intValue,0, bitmap.width,0,0, bitmap.width,bitmap.height)
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val input = intValue[pixel++]

                byteBuffer.putFloat((((input.shr(16) and 0xFF) - imageMean) / imageStd))
                byteBuffer.putFloat((((input.shr(8) and 0xFF) - imageMean) / imageStd))
                byteBuffer.putFloat((((input and 0xFF) - imageMean) / imageStd))
            }
        }
        return byteBuffer
    }

    private fun getSortedResult(labelProbArray: Array<FloatArray>): List<Recognition>{
        Log.d("Classifier", "List Size:(%d, %d, %d)".format(labelProbArray.size, labelProbArray[0].size, labelList.size))

        val pq = PriorityQueue(
            maxResult,
            Comparator<Recognition> {
                (_, _, confidence1), (_, _, confidence2)
                -> confidence1.compareTo(confidence2) * -1
            }
        )

        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence >= threshHold) {
                pq.add(Recognition("" + i,
                if(labelList.size > i) labelList[i] else "Unknown", confidence)
                )
            }
        }

        Log.d("Classifier", "pqsize:(%d".format(pq.size))
        val recognitions = ArrayList<Recognition>()
        val recognitionsSize = min(pq.size, maxResult)
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }
        return recognitions
    }

}