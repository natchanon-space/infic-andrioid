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
        scene.title = title
        scene.story = story
        scene.choices = choices
        scene.inputParameters = inputParameters
        scene.isEndingScene = isEndingScene

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

    fun getSceneByUUID(uuid: UUID): Scene {
        for (scene in scenes) {
            if (scene.uuid == uuid) {
                return scene
            }
        }
        throw Exception("Scene with UUID <$uuid> is not found.")
    }

    fun getJsonName(): String {
        return "$title-by-$author.json"
    }
}
