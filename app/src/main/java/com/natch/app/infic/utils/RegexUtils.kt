package com.natch.app.infic.utils

import android.util.Log

fun replaceParameters(string: String, parameters: Map<String, String>): String {
    var temp = string
    parameters.forEach {
        Log.d("DEBUG", "replace {${it.key}} with ${it.value}")
        val pattern = it.key.toParameterPattern()
        temp = temp.replace(
            pattern,
            it.value
        )
    }
    return temp
}

fun replaceString(string: String, replaceString: String, pattern: Regex): String {
    return string.replace(pattern, replaceString)
}

fun String.toParameterPattern(): Regex {
    return Regex("\\{${this}\\}")
}

fun String.toParameterBracket(): String {
    return "{${this}}"
}