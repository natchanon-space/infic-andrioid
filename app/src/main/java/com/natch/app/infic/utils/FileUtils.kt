package com.natch.app.infic.utils

import android.content.Context
import android.util.Log
import com.natch.app.infic.model.Fiction
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

fun writeFictionToJsonFile(fiction: Fiction, context: Context) {
    val path = context.filesDir.path
    val fileName = fiction.getJsonName()
    try {
        val writer = FileOutputStream(File(path, fileName))
        val json = Json.encodeToString(fiction)
        writer.write(json.toByteArray())
        writer.close()
        Log.d(TAG, "saved to $path/$fileName")
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

fun readAllFictionsFromDir(context: Context): MutableList<Fiction> {
    val fileNames = context.filesDir.listFiles()
    val fictions = mutableListOf<Fiction>()

    fileNames.forEach { fileName ->
        try {
            val inputStream = File(fileName.path).inputStream()
            val rawFiction = inputStream.bufferedReader().use { it.readText() }
            val parsedFiction = Json.decodeFromString<Fiction>(rawFiction)
            fictions.add(parsedFiction)
            Log.d(TAG, "Loaded: ${parsedFiction.title} from (${fileName.path})")
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    return fictions
}

fun deleteFictionFromDir(fiction: Fiction, context: Context) {
    try {
        context.deleteFile(fiction.getJsonName())
        Log.d(TAG, "Deleted: ${fiction.getJsonName()} from directory")
    } catch (error: Exception) {
        error.printStackTrace()
    }
}

fun fictionToJson(fiction: Fiction): String {
    return Json.encodeToString(fiction)
}

fun jsonToFiction(jsonString: String): Fiction {
    return Json.decodeFromString(jsonString)
}

const val TAG = "FileUtils"
