package models

data class ReIntObject(
    var id : ReObjectId = ReObjectId.NONE,
    var square: Double = 0.0,
    var price: Int = 0,
    var district: ReDistrict = ReDistrict.NONE,
    var rooms: Int = 0
) {

    companion object {
        val NONE = ReIntObject()
    }
}
