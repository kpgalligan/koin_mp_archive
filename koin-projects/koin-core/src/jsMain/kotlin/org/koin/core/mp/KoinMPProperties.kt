package org.koin.core.mp

actual class KoinMPProperties(val map: Map<String, String>) {
    actual val size: Int
        get() = map.size
}

actual fun KoinMPProperties.toMap(): Map<String, String> {
    return map.toMap()
}
