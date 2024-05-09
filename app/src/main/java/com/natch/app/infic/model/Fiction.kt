package com.natch.app.infic.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class Fiction constructor(
    var title: String,
    var author: String
) {
    // collection of parameter name and its default value
    val parameters = mutableMapOf<String, String>()
    val scenes = mutableListOf<Scene>()
    @Serializable(with = UUIDSerializer::class)
    var firstSceneUUID: UUID? = null

    fun addParameter(parameterName: String, defaultValue: String = "") {
        parameters[parameterName] = defaultValue
    }

    fun updateParameter(parameterName: String, updateValue: String) {
        parameters[parameterName] = updateValue
    }

    fun deleteParameter(parameterName: String) {
        parameters.remove(parameterName)
    }

    fun addScene(title: String) {
        var uuid: UUID = UUID.randomUUID()

        // check uniqueness of exists uuid
        var isUnique = true
        while (true) {
            for (scene in scenes) {
                if (scene.uuid == uuid) {
                    isUnique = false
                    uuid = UUID.randomUUID()
                }
            }
            if (isUnique) {
                break
            }
        }

        scenes.add(Scene(title, uuid))
    }

    fun updateScene(
        scene: Scene,
        title: String,
        story: String,
        choices: MutableList<Choice>,
        inputParameters: MutableList<String>,
        isEndingScene: Boolean,
        isFirstScene: Boolean = false
    ) {
        // update scene attributes
        val update = getSceneByUUID(scene.uuid)!!
        update.title = title
        update.story = story
        update.choices = choices
        update.inputParameters = inputParameters
        update.isEndingScene = isEndingScene

        // update first scene on check
        if (isFirstScene) {
            firstSceneUUID = scene.uuid

            // allowing one first scene per fiction
            for (s in scenes) {
                s.isFirstScene = (s.uuid == firstSceneUUID)
            }
        }
    }

    fun deleteScene(scene: Scene) {
        scenes.remove(scene)
    }

    fun getSceneByUUID(uuid: UUID?): Scene? {
        if (uuid == null) {
            return null
        }
        for (scene in scenes) {
            if (scene.uuid == uuid) {
                return scene
            }
        }
        return null
    }

    fun getJsonName(): String {
        return "$title-by-$author.json"
    }
}
