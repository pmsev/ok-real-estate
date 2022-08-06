package mappers

import ReAdGremlinConst.FIELD_ADDRESS
import ReAdGremlinConst.FIELD_DESCRIPTION
import ReAdGremlinConst.FIELD_DISTRICT
import ReAdGremlinConst.FIELD_LOCATION_LATITUDE
import ReAdGremlinConst.FIELD_LOCATION_LONGITUDE
import ReAdGremlinConst.FIELD_LOCK
import ReAdGremlinConst.FIELD_PRICE
import ReAdGremlinConst.FIELD_ROOMS
import ReAdGremlinConst.FIELD_SELLER_ID
import ReAdGremlinConst.FIELD_SQUARE
import ReAdGremlinConst.FIELD_TITLE
import WrongEnumException
import models.*
import models.location.Degree
import models.location.ReoLocation
import org.apache.tinkerpop.gremlin.structure.T

fun Map<Any?, Any?>.toReAd(): ReAd = ReAd(
    id = (this[T.id] as? String)?.let { ReAdId(it) } ?: ReAdId.NONE,
    sellerId = (this[FIELD_SELLER_ID] as? String)?.let { ReUserId(it) } ?: ReUserId.NONE,
    lock = (this[FIELD_LOCK] as? String)?.let { ReAdLock(it) } ?: ReAdLock.NONE,
    title = (this[FIELD_TITLE] as? String) ?: "",
    description = (this[FIELD_DESCRIPTION] as? String) ?: "",

    reIntObject = this@toReAd.toReIntObject()


)

fun Map<Any?, Any?>.toReIntObject(): ReIntObject = ReIntObject(
    square = this[FIELD_SQUARE] as Double,
    price = this[FIELD_PRICE] as Int,
    address = this[FIELD_ADDRESS] as String,
    rooms = this[FIELD_ROOMS] as Int,
    district = when (val value = this[FIELD_DISTRICT] as String?) {
        null -> ReDistrict.NONE
        else -> try {
            ReDistrict.valueOf(value)
        } catch (e: Exception) {
            throw WrongEnumException("Can't convert object from DB." + " $value cannot be converted to ${ReDistrict::class.java}")
        }
    },
    location = toReoLocation()
)

fun Map<Any?, Any?>.toReoLocation(): ReoLocation = ReoLocation(
    latitude = when (val value  = this[FIELD_LOCATION_LATITUDE]) {
        null -> Degree.NONE
        else -> Degree(value as Double)
    },
    longitude = when (val value = this[FIELD_LOCATION_LONGITUDE]) {
        null -> Degree.NONE
        else -> Degree(value as Double)
    },
)


