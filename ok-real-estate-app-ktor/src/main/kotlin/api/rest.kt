package api

import io.ktor.server.application.*
import io.ktor.server.routing.*
import services.ReAdService
import services.ReOffersService

fun Route.reAd(reAdService: ReAdService) {
    route("ad") {
        post("create") {
            call.createAd(reAdService)
        }
        post("read") {
            call.readAd(reAdService)
        }
        post("update") {
            call.updateAd(reAdService)
        }
        post("delete") {
            call.deleteAd(reAdService)
        }
        post("search") {
            call.searchAd(reAdService)
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
