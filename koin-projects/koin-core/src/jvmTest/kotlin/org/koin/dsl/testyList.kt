package org.koin.dsl

import co.touchlab.stately.collections.frozenLinkedList

actual fun <T> testyList(): MutableList<T> = frozenLinkedList()
