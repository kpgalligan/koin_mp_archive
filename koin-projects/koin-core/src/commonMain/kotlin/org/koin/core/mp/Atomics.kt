package org.koin.core.mp

import kotlin.reflect.KProperty

internal expect class FrozenDelegate<T>(t:T){
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}

internal expect class ThreadLocalDelegate<T>(){
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)

    internal fun cleanUp()
}
