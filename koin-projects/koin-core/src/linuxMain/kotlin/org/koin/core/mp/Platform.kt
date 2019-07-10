package org.koin.core.mp

import kotlinx.cinterop.get
import kotlinx.cinterop.toKString
import platform.posix.__environ

internal actual fun getSystemEnvironmentProperties(): Map<String, String> {
    val source = __environ ?: return emptyMap()
    val target = mutableMapOf<String, String>()
    for (i in 0..Int.MAX_VALUE) {
        val value = source.get(i) ?: break
        val str = value.toKString()
        val index = str.indexOf("=")
        if (index != -1) {
            target += str.substring(0, index) to str.substring(index + 1)
        }
    }
    return target
}
