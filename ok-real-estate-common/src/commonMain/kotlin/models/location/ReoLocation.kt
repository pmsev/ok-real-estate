package models.location

/**
 * Real Estate Object Location - геолокация объекта недвижимости
 */
data class ReoLocation(
    var latitude: Degree = Degree.NONE,
    var longitude: Degree = Degree.NONE
) {
    companion object {
        val  NONE = ReoLocation(Degree.NONE, Degree.NONE)
    }
}