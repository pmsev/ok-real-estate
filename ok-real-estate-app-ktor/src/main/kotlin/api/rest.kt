package api

import io.ktor.server.application.*
import io.ktor.server.routing.*
import reLogger
import services.ReAdService
import services.ReOffersService

private val logger = reLogger(Route::reAd::class.java)

fun Route.reAd(reAdService: ReAdService) {
    route("ad") {
        post("create") {
            call.createAd(reAdService, logger)
        }
        post("read") {
            call.readAd(reAdService, logger)
        }
        post("update") {
            call.updateAd(reAdService, logger)
        }
        post("delete") {
            call.deleteAd(reAdService, logger)
        }
        post("search") {
            call.searchAd(reAdService, logger)
        }
    }
}

fun Route.reOffer(offerService: ReOffersService) {
    route("ad") {
        post("offers") {
            call.offersAd(offerService)
        }
    }
}
