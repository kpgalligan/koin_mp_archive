package org.koin.multiplatform

expect fun <T> doInOtherThread(block: () -> T): T
