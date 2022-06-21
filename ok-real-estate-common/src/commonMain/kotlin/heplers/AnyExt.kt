package heplers

import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

@Throws(IllegalAccessException::class, ClassCastException::class)

inline fun <reified T> Any.getField(fieldName: String): T? {
    this::class.memberProperties.forEach { kCallable ->
        if (fieldName == kCallable.name) {
            return kCallable.getter.call(this) as T?
        }
    }
    return null
}

inline fun <reified T> Any.setField(fieldName: String, value: T) {
    val member = this::class.declaredMemberProperties.find { it.name == fieldName } as KMutableProperty<*>?
        ?: error("Wrong field name")
    member.setter.call(this, value)
}