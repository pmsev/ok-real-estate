package api

import ReContext
import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.realestate.api.v1.models.AdOffersRequest
import services.ReOffersService
import toTransportOffers

suspend fun ApplicationCall.offersAd(offerService: ReOffersService) {
    val offersAdRequest = receive<AdOffersRequest>()
    respond(
        ReContext().apply { fromTransport(offersAdRequest) }.let {
            offerService.searchOffers(it, ::buildError)
        }.toTransportOffers()
    )
}
