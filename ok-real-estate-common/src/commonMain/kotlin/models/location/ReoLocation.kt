package models.location

/**
 * Real Estate Object Location - геолокация объекта недвижимости
 */
data class ReoLocation(
    var latitude: Degree = Degree.NONE,
    var longitude: Degree = Degree.NONE
) {
    fun asString(): String = latitude.asDouble().toString() + ";" + longitude.asDouble().toString()

    companion object {
        fun fromString(location: String?): ReoLocation = if (location.isNullOrEmpty()) NONE else {
            val values = location.split(";")
            if (values.size != 2) NONE
            else {
                ReoLocation(Degree(values[0].toDouble()), Degree(values[1].toDouble()))
            }
        }

        val NONE = ReoLocation(Degree.NONE, Degree.NONE)
    }
}