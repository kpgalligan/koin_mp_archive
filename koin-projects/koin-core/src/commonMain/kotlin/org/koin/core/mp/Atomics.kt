package org.koin.core.mp

import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.freeze
import kotlin.reflect.KProperty

internal class FrozenDelegate<T>(t:T){
    private val delegateReference = AtomicReference(t.freeze())
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = delegateReference.get()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        delegateReference.set(value.freeze())
    }
}

internal class FrozenBoolean(b:Boolean) {
    private val delegateReference = AtomicBoolean(b)
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean = delegateReference.value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        delegateReference.value = value
    }
}
