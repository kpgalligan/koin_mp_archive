package org.koin.core.mp

import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

internal actual class FrozenDelegate<T> actual constructor(t: T) {
    private val delegateReference = AtomicReference(t)
    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T = delegateReference.get()

    actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        delegateReference.set(value)
    }
}
