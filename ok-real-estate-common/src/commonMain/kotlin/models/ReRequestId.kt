package models

@JvmInline
value class ReRequestId(private val id: String) {

    fun asString() = id

    companion object{
        val NONE = ReRequestId("")
    }
}