package model

import models.ReDistrict
import models.ReIntObject
import models.ReObjectId
import models.location.ReoLocation

class ReIntEntityObject(
    var id: String? = null,
    var square: Double? = null,
    var price: Int? = null,
    var district: String? = null,
    var address: String? = null,
    var rooms: Int? = null,
    var location: String? = null

) {

    constructor(reIntObject: ReIntObject) : this(
        id = reIntObject.id.asString().takeIf { it.isNotBlank() },
        square = reIntObject.square,
        price = reIntObject.price,
        district = reIntObject.district.name.takeIf { it.isNotBlank() },
        address = reIntObject.address.takeIf { it.isNotBlank() },
        rooms = reIntObject.rooms,
        location = reIntObject.location.asString()
    )


}

fun ReIntEntityObject?.toInternal(): ReIntObject = if (this == null) ReIntObject.NONE else ReIntObject(
    id = id?.let { ReObjectId(it) } ?: ReObjectId.NONE,
    square = square ?: 0.0,
    price = price ?: 0,
    district = district?.let { ReDistrict.valueOf(it) } ?: ReDistrict.NONE,
    address = address ?: "",
    rooms = rooms ?: 0,
    location = ReoLocation.fromString(location)
)


fun ReIntObject.toEntity(): ReIntEntityObject? = when (this) {
    ReIntObject.NONE -> null
    else -> ReIntEntityObject(this)
}
