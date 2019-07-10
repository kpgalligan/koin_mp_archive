package org.koin.core.mp

actual class KoinMPLock<T> actual constructor(override val subject: T) : AbstractKoinMPLock<T>()
