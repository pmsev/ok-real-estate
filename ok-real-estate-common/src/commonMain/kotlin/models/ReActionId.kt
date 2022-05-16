package models

@JvmInline
value class ReActionId(private val id: String) {
    companion object{
        val NONE = ReActionId("")
    }
}
