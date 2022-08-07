package api

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import services.ReAdService
import services.ReOffersService

internal fun Routing.openApi(adService: ReAdService, offerService: ReOffersService) {
    authenticate("auth-jwt") {
        reAd(adService)
        reOffer(offerService)
    }
}

