package models

import models.location.ReoLocation

/**Объект недвижимости**/
data class ReIntObject(
    var id: ReObjectId = ReObjectId.NONE,
    var square: Double = 0.0,
    var price: Int = 0,
    var district: ReDistrict = ReDistrict.NONE,
    var address: String = "",
    var rooms: Int = 0,
    var location: ReoLocation = ReoLocation.NONE

) {
    fun deepCopy() = ReIntObject(
        id = this@ReIntObject.id,
        square = this@ReIntObject.square,
        price = this@ReIntObject.price,
        district = this@ReIntObject.district,
        address = this@ReIntObject.address,
        rooms = this@ReIntObject.rooms,
        location = this@ReIntObject.location
    )

    companion object {
        val NONE = ReIntObject()
    }
}
