package org.koin.core.mp

import co.touchlab.stately.concurrency.ThreadLocalRef
import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze
import kotlin.reflect.KProperty

internal actual class FrozenDelegate<T> actual constructor(t: T) {
    private val delegateReference = AtomicReference(t.freeze())
    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T = delegateReference.value

    actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        delegateReference.value = value.freeze()
    }
}

internal actual class ThreadLocalDelegate<T> actual constructor() {
    private val ref = ThreadLocalRef<T?>()
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
