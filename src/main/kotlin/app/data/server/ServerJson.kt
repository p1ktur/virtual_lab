package app.data.server

import kotlinx.serialization.json.*

object ServerJson {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
        coerceInputValues = true
    }

    fun get(): Json = json
}