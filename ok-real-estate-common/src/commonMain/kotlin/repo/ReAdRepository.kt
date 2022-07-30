package repo

interface ReAdRepository {
    suspend fun createReAd(rq: DbReAdRequest): DbReAdResponse
    suspend fun readReAd(rq: DbReAdIdRequest): DbReAdResponse
    suspend fun updateReAd(rq: DbReAdRequest): DbReAdResponse
    suspend fun deleteReAd(rq: DbReAdIdRequest): DbReAdResponse
    suspend fun searchReAd(rq: DbReAdFilterRequest): DbReAdsResponse
    companion object {
        val NONE = object : ReAdRepository {
            override suspend fun createReAd(rq: DbReAdRequest): DbReAdResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readReAd(rq: DbReAdIdRequest): DbReAdResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateReAd(rq: DbReAdRequest): DbReAdResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteReAd(rq: DbReAdIdRequest): DbReAdResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchReAd(rq: DbReAdFilterRequest): DbReAdsResponse {
                TODO("Not yet implemented")
            }
        }
    }

}