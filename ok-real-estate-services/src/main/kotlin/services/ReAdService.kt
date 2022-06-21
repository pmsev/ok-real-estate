package services

import ReAdProcessor
import ReContext

class ReAdService {

    private val processor = ReAdProcessor()

    suspend fun createAd(context: ReContext) = processor.exec(context)
    suspend fun readAd(context: ReContext) = processor.exec(context)
    suspend fun updateAd(context: ReContext) = processor.exec(context)
    suspend fun deleteAd(context: ReContext) = processor.exec(context)
    suspend fun searchAd(context: ReContext) = processor.exec(context)

}