package ru.otus.otuskotlin.realestate.api.v1
import ru.otus.otuskotlin.realestate.api.v1.models.Response

fun apiV1ResponseSerialize(response: Response): String = jacksonMapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T : Response> apiV1ResponseDeserialize(json: String): T =
    jacksonMapper.readValue(json, Response::class.java) as T
