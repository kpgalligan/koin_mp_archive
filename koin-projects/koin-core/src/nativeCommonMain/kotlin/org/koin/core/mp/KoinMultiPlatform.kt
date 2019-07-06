package org.koin.core.mp

import co.touchlab.stately.collections.frozenHashMap
import co.touchlab.stately.collections.frozenHashSet
import co.touchlab.stately.collections.frozenLinkedList
import platform.Foundation.NSProcessInfo
import kotlin.reflect.KClass
import kotlin.system.getTimeNanos

actual object KoinMultiPlatform {
    actual fun <K, V> emptyMutableMap(): MutableMap<K, V> = frozenHashMap()

    actual fun stackTrace(throwable: Throwable): List<String> {
        return throwable.getStackTrace().toList()
    }

    actual fun nanoTime(): Long {
        return getTimeNanos()
    }

    actual fun getSystemProperties(): Map<String, String> {
        return emptyMap()
    }

    actual fun getSystemEnvironmentProperties(): Map<String, String> {
        val source = NSProcessInfo.processInfo.environment
        val target = mutableMapOf<String, String>()

        source.entries.forEach { entry ->
            val key = entry.key
            val value = entry.value
            //They're never null, but just in case
            if(key != null && value != null) {
                target.put(key.toString(), value.toString())
            }
        }

        return target
    }

    actual fun loadResourceString(fileName: String): String? {
        // TODO
        println("TODO: KoinMultiPlatform.loadResourceString is not yet implemented")
        return null
    }

    actual fun parseProperties(content: String): KoinMPProperties {
        // TODO
        println("TODO: KoinMultiPlatform.parseProperties is not yet implemented")
        return KoinMPProperties(emptyMap())
    }

    actual fun className(kClass: KClass<*>): String {
        return kClass.qualifiedName ?: "KClass@${hashCode()}"
    }

    actual fun printStackTrace(throwable: Throwable) {
        stackTrace(throwable).forEach(::println)
    }

    actual fun <T> emptyMutableSet(): MutableSet<T> = frozenHashSet()

    actual fun <T> emptyMutableList(): MutableList<T> = frozenLinkedList()
}
