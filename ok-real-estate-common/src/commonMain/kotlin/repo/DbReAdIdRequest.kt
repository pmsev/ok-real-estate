package repo

import models.ReAd
import models.ReAdId
import models.ReAdLock

data class DbReAdIdRequest (
    val id: ReAdId,
    val lock: ReAdLock = ReAdLock.NONE,
) {
    constructor(reAd: ReAd): this(reAd.id, reAd.lock)
}
