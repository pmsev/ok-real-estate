package models

data class ReAdFilter(
    var searchString: String = "",
    var sellerId: ReUserId = ReUserId.NONE,
    var status: ReStatus = ReStatus.NONE
)
