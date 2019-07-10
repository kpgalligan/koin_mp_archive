package org.koin.core.mp

import kotlin.reflect.KProperty

internal actual class FrozenDelegate<T> actual constructor(t: T) {
    var tVal = t
    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T = tVal

    actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        tVal = value
    }
}
