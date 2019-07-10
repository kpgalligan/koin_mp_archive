package org.koin.core.mp

import kotlin.browser.window
import kotlin.js.Date
import kotlin.reflect.KClass


actual object KoinMultiPlatform {
    actual fun <K, V> emptyMutableMap(): MutableMap<K, V> = mutableMapOf()

    actual fun <T> emptyMutableSet(): MutableSet<T> = mutableSetOf()

    actual fun <T> emptyMutableList(): MutableList<T> = mutableListOf()

    actual fun stackTrace(throwable: Throwable): List<String> = emptyList()

    actual fun nanoTime(): Long = Date().getTime().toLong() * 1_000_000

    actual fun getSystemProperties(): Map<String, String> = emptyMap()

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
        return kClass.simpleName ?: "KClass@${hashCode()}"
    }

    actual fun printStackTrace(throwable: Throwable) {
        println(throwable.message)
        println(throwable.toString())
    }
}

internal actual fun getSystemEnvironmentProperties(): Map<String, String> = emptyMap()

actual fun <T> T.freeze(): T = this
