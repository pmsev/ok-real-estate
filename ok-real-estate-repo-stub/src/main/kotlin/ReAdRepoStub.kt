import repo.*

class ReAdRepoStub : ReAdRepository {
    override suspend fun createReAd(rq: DbReAdRequest): DbReAdResponse {
        return DbReAdResponse(
            result = ReAdStub.prepareResult {  },
            isSuccess = true,
        )
    }

    override suspend fun readReAd(rq: DbReAdIdRequest): DbReAdResponse {
        return DbReAdResponse(
            result = ReAdStub.prepareResult {  },
            isSuccess = true,
        )

    }

    override suspend fun updateReAd(rq: DbReAdRequest): DbReAdResponse {
        return DbReAdResponse(
            result = ReAdStub.prepareResult {  },
            isSuccess = true,
        )

    }

    override suspend fun deleteReAd(rq: DbReAdIdRequest): DbReAdResponse {
        return DbReAdResponse(
            result = ReAdStub.prepareResult {  },
            isSuccess = true,
        )

    }

    override suspend fun searchReAd(rq: DbReAdFilterRequest): DbReAdsResponse {
        return DbReAdsResponse(
            result = ReAdStub.prepareSearchList("111"),
            isSuccess = true,
        )

    }
}