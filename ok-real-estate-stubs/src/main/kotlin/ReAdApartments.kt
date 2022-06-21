import models.*
import models.location.Degree
import models.location.ReoLocation

object ReAdApartments {
    val AD_TWO_BEDROOM_APART: ReAd
        get() = ReAd(
            id = ReAdId(id = "777"),
            title = "Евродвушка в лучшем районе. Срочно. Дешево.",
            description = "Евродвушка с евроремонтом в Южном районе Нска.",
            sellerId = ReUserId(id = "888"),
            status = ReStatus.OPENED,
            reIntObject = ReIntObject(
                id = ReObjectId("777"),
                square = 55.5,
                price = 10_000_000,
                district = ReDistrict.SOUTH,
                address = "ул. Южная, д. 5, строение 1",
                rooms = 2,
                location = ReoLocation(latitude = Degree(43.6), longitude = Degree(39.7167))
            )
        )


}