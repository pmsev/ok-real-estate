package services

import ReContext
import models.ReError
import stubs.ReStubs
import stubs.RealEstateObject

class ReOffersService {
    fun searchOffers(context: ReContext, buildError: () -> ReError): ReContext {
        val request = context.adRequest

        return when (context.stubCase) {
            ReStubs.SUCCESS -> context.successResponse {
                adResponse = RealEstateObject.getModel { id = context.adRequest.id }
                adsResponse.addAll(RealEstateObject.getModels().onEach { it.id = request.id })
            }
            else -> {
                context.errorResponse(buildError) {
                    it.copy(field = "ad.id", message = notFoundError(request.id.asString()))
                }
            }
        }
    }
}