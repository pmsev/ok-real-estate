package models

@JvmInline
value class ReAdLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ReAdLock("")
    }
}
