package com.natch.app.infic.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class Scene constructor(
    var title: String,
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID?
) {
    var story: String = ""
    var choices = mutableListOf<Choice>()
    var inputParameters = mutableListOf<String>()
    var isEndingScene: Boolean = false

    fun addChoice(choice: Choice) {
        choices.add(choice)
    }
}