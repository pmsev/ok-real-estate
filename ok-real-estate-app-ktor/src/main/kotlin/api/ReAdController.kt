package api

import ReLogWrapper
import io.ktor.server.application.*
import models.ReCommand
import ru.otus.otuskotlin.realestate.api.v1.models.*
import services.ReAdService

suspend fun ApplicationCall.createAd(adService: ReAdService, logger: ReLogWrapper) =
    controllerHelper<AdCreateRequest, AdCreateResponse>(
        logger = logger,
        logId = "ad-create",
        command = ReCommand.CREATE
    ) {
        adService.createAd(this)
    }


suspend fun ApplicationCall.readAd(service: ReAdService, logger: ReLogWrapper) =
    controllerHelper<AdReadRequest, AdReadResponse>(
        logger = logger,
        logId = "ad-create", command = ReCommand.READ
    ) {
        service.readAd(this)
    }

suspend fun ApplicationCall.updateAd(service: ReAdService, logger: ReLogWrapper) =
    controllerHelper<AdUpdateRequest, AdUpdateResponse>(
        logger = logger,
        logId = "ad-create", command = ReCommand.UPDATE
    ) {
        service.updateAd(this)
    }

suspend fun ApplicationCall.deleteAd(service: ReAdService, logger: ReLogWrapper) =
    controllerHelper<AdDeleteRequest, AdDeleteResponse>(
        logger = logger,
        logId = "ad-create", command = ReCommand.DELETE
    ) {
        service.deleteAd(this)
    }

suspend fun ApplicationCall.searchAd(adService: ReAdService, logger: ReLogWrapper) =
    controllerHelper<AdSearchRequest, AdSearchResponse>(
        logger = logger,
        logId = "ad-create", command = ReCommand.SEARCH
    ) {
        adService.searchAd(this)
    }
