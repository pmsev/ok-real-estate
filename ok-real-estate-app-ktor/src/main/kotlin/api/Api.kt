package api

import io.ktor.server.routing.*
import services.ReAdService
import services.ReOffersService

internal fun Routing.openApi(adService: ReAdService, offerService: ReOffersService) {
    reAd(adService)
    reOffer(offerService)
}

