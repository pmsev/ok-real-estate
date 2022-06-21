@file:Suppress("UNCHECKED_CAST")

package ru.otus.otuskotlin.realestate.api.v1
import ru.otus.otuskotlin.realestate.api.v1.models.Request

fun apiV1RequestSerialize(request: Request): String = jacksonMapper.writeValueAsString(request)

fun <T : Request> apiV1RequestDeserialize(json: String): T =
    jacksonMapper.readValue(json, Request::class.java) as T
