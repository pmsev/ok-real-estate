package models.security
import models.ReUserId


data class RePrincipalModel(
    val id: ReUserId = ReUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<ReUserGroups> = emptySet()
) {
    companion object {
        val NONE = RePrincipalModel()
    }
}
