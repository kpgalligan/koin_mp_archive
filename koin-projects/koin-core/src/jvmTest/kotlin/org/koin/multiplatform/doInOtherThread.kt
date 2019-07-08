package org.koin.multiplatform

actual fun <T> doInOtherThread(block: () -> T): T = block()
