package services

import ReAdProcessor
import ReContext
import models.ReSettings

class ReAdService(val settings: ReSettings) {

    private val processor = ReAdProcessor(settings)

    suspend fun exec(context: ReContext) = processor.exec(context)

    suspend fun createAd(context: ReContext) = processor.exec(context)
    suspend fun readAd(context: ReContext) = processor.exec(context)
    suspend fun updateAd(context: ReContext) = processor.exec(context)
    suspend fun deleteAd(context: ReContext) = processor.exec(context)
    suspend fun searchAd(context: ReContext) = processor.exec(context)

}