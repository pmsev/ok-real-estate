package models

@JvmInline
value class ReAdId (private val id: String) {

    fun asString() = id

    companion object {
        val NONE = ReAdId("")
    }
}
