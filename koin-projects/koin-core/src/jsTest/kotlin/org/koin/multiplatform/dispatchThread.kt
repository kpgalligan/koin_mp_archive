package org.koin.multiplatform

actual fun <T> dispatchThread(block: () -> T): T = block()
