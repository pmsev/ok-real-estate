package models

import models.location.ReoLocation

data class ReIntObject(
    var id : ReObjectId = ReObjectId.NONE,
    var square: Double = 0.0,
    var price: Int = 0,
    var district: ReDistrict = ReDistrict.NONE,
    var address: String = "",
    var rooms: Int = 0,
    var location: ReoLocation = ReoLocation.NONE

) {

    companion object {
        val NONE = ReIntObject()
    }
}
