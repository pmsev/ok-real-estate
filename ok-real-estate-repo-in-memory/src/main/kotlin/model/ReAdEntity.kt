package model

import models.*

class ReAdEntity(
    var id:  String? = null,
    var title: String? = null,
    var description: String? = null,
    var sellerId: String? = null,
    var reIntObject: ReIntEntityObject? = null,
    var status: String? = null,
    var lock: String? = null,
) {
    constructor(model: ReAd): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        sellerId = model.sellerId.asString().takeIf { it.isNotBlank() },
        reIntObject = model.reIntObject.toEntity(),
        status = model.status.name.takeIf { it.isNotBlank() },
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() =
        ReAd(
        id = id?.let { ReAdId(it) }?: ReAdId.NONE,
        title = title?: "",
        description = description?: "",
        sellerId = sellerId?.let { ReUserId(it) }?: ReUserId.NONE,
        reIntObject = reIntObject.toInternal(),
        status = status?.let {ReStatus.valueOf(it)} ?: ReStatus.NONE,
        lock = lock?.let { ReAdLock(it) } ?: ReAdLock.NONE,
    )

}