package org.koin.multiplatform

expect fun <T> dispatchThread(block: () -> T): T
