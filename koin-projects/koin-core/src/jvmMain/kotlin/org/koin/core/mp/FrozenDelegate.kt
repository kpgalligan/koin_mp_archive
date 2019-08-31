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

internal actual class ThreadLocalDelegate<T> actual constructor() {
    private val ref = ThreadLocal<T?>()
    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return ref.get()!!
    }

    actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        ref.set(value)
    }

    internal actual fun cleanUp() {
        ref.remove()
    }
}
