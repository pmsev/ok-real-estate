package models

@JvmInline
value class ReUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ReUserId("")
    }
}
