package models

@JvmInline
value class ReObjectId (private val id: String) {

    fun asString() = id

    companion object {
        val NONE = ReObjectId("")
    }

}
