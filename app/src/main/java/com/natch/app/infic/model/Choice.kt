package com.natch.app.infic.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class Choice constructor(
    var text: String,
    @Serializable(with = UUIDSerializer::class)
    var nextSceneUUID: UUID?

    // TODO: make choice more dynamic
)