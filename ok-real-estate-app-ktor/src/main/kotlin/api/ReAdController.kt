package api

import ReContext
import fromTransport
import heplers.asReError
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import models.ReCommand
import models.ReState
import ru.otus.otuskotlin.realestate.api.v1.models.*
import services.ReAdService
import toTransportAd

suspend fun ApplicationCall.createAd(adService: ReAdService) {
    val ctx = ReContext(
        timeStart = Clock.System.now(),
    )
    try {
        val request = receive<AdCreateRequest>()
        ctx.fromTransport(request)
        adService.createAd(ctx)
        val response = ctx.toTransportAd()
        respond(response)
    } catch (e: Throwable) {
        ctx.command = ReCommand.CREATE
        ctx.state = ReState.FAILING
        ctx.errors.add(e.asReError())
        adService.createAd(ctx)
        val response = ctx.toTransportAd()
        respond(response)
    }
}

suspend fun ApplicationCall.readAd(service: ReAdService) =
    controllerHelper<AdReadRequest, AdReadResponse>(ReCommand.READ) {
        service.readAd(this)
    }

suspend fun ApplicationCall.updateAd(service: ReAdService) =
    controllerHelper<AdUpdateRequest, AdUpdateResponse>(ReCommand.UPDATE) {
        service.updateAd(this)
    }

suspend fun ApplicationCall.deleteAd(service: ReAdService) =
    controllerHelper<AdDeleteRequest, AdDeleteResponse>(ReCommand.DELETE) {
        service.deleteAd(this)
    }

suspend fun ApplicationCall.searchAd(adService: ReAdService) =
    controllerHelper<AdSearchRequest, AdSearchResponse>(ReCommand.SEARCH) {
        adService.searchAd(this)
    }
