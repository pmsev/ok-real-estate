package services

import ReContext
import models.ReAdId
import models.ReError
import models.ReStatus
import models.ReWorkMode
import stubs.ReStubs
import stubs.RealEstateObject

class ReAdService {

    fun createAd(reContext: ReContext): ReContext {
        val response = when (reContext.workMode) {
            ReWorkMode.PROD -> TODO()
            ReWorkMode.TEST -> reContext.adRequest
            ReWorkMode.STUB -> RealEstateObject.getModel()
        }
        return reContext.successResponse {
            adResponse = response
        }
    }

    fun readAd(mpContext: ReContext, buildError: () -> ReError): ReContext {
        val requestedId = mpContext.adRequest.id

        return when (mpContext.stubCase) {
            ReStubs.SUCCESS -> mpContext.successResponse {
                adResponse = RealEstateObject.getModel().apply { id = requestedId }
            }
            else -> mpContext.errorResponse(buildError) {
                it.copy(field = "ad.id", message = notFoundError(requestedId.asString()))
            }
        }
    }

    fun updateAd(context: ReContext, buildError: () -> ReError) = when (context.stubCase) {
        ReStubs.SUCCESS -> context.successResponse {
            adResponse = RealEstateObject.getModel {
                if (adRequest.status != ReStatus.NONE) status = adRequest.status
                if (adRequest.id != ReAdId.NONE) id = adRequest.id
                if (adRequest.title.isNotEmpty()) title = adRequest.title
                if (adRequest.description.isNotEmpty()) title = adRequest.description
            }
        }
        else -> context.errorResponse(buildError) {
            it.copy(field = "ad.id", message = notFoundError(context.adRequest.id.asString()))
        }
    }

    fun deleteAd(context: ReContext, buildError: () -> ReError): ReContext = when (context.stubCase) {
        ReStubs.SUCCESS -> context.successResponse {
            adResponse = RealEstateObject.getModel { id = context.adRequest.id }
        }
        else -> context.errorResponse(buildError) {
            it.copy(
                field = "ad.id",
                message = notFoundError(context.adRequest.id.asString())
            )
        }
    }

    fun searchAd(context: ReContext, buildError: () -> ReError): ReContext {
        val filter = context.adFilterRequest

        val searchableString = filter.searchString

        return when (context.stubCase) {
            ReStubs.SUCCESS -> context.successResponse {
                adsResponse.addAll(
                    RealEstateObject.getModels()
                )
            }
            else -> context.errorResponse(buildError) {
                it.copy(
                    message = "Nothing found by $searchableString"
                )
            }
        }
    }

}