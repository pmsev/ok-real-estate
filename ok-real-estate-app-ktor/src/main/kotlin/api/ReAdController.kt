package api

import ReContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.realestate.api.v1.models.*
import services.ReAdService
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportSearch
import toTransportUpdate

suspend fun ApplicationCall.createAd(reAdService: ReAdService) {
    val createAdRequest = receive<AdCreateRequest>()
    respond(
        ReContext().apply { fromTransport(createAdRequest) }.let {
            reAdService.createAd(it)
        }.toTransportCreate()
    )
}

suspend fun ApplicationCall.readAd(reAdService: ReAdService) {
    val readAdRequest = receive<AdReadRequest>()
    respond(
        ReContext().apply { fromTransport(readAdRequest) }.let {
            reAdService.readAd(it, ::buildError)
        }.toTransportRead()
    )
}

suspend fun ApplicationCall.updateAd(reAdService: ReAdService) {
    val updateAdRequest = receive<AdUpdateRequest>()
    respond(
        ReContext().apply { fromTransport(updateAdRequest) }.let {
            reAdService.updateAd(it, ::buildError)
        }.toTransportUpdate()
    )
}

suspend fun ApplicationCall.deleteAd(reAdService: ReAdService) {
    val deleteAdRequest = receive<AdDeleteRequest>()
    respond(
        ReContext().apply { fromTransport(deleteAdRequest) }.let {
            reAdService.deleteAd(it, ::buildError)
        }.toTransportDelete()
    )
}

suspend fun ApplicationCall.searchAd(reAdService: ReAdService) {
    val searchAdRequest = receive<AdSearchRequest>()
    respond(
        ReContext().apply { fromTransport(searchAdRequest) }.let {
            reAdService.searchAd(it, ::buildError)
        }.toTransportSearch()
    )
}
