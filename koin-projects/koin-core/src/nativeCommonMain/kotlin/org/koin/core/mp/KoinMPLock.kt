package org.koin.core.mp

import co.touchlab.stately.concurrency.AtomicInt
import co.touchlab.stately.concurrency.Lock
import co.touchlab.stately.concurrency.close
import co.touchlab.stately.concurrency.withLock

actual data class KoinMPLock<T> actual constructor(override val subject: T) :
    AbstractKoinMPLock<T>() {

    private val lock = Lock()

    override operator fun <R> invoke(handler: T.() -> R): R = lock.withLock {
        subject.handler()
    }

    override fun close() {
        lock.close()
    }
}
