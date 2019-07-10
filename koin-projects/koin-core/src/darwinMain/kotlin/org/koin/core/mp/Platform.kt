package org.koin.core.mp

import platform.Foundation.NSProcessInfo

internal actual fun getSystemEnvironmentProperties(): Map<String, String> {
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
