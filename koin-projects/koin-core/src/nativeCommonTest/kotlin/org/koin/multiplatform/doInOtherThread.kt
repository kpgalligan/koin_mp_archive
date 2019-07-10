package org.koin.multiplatform

import kotlin.native.concurrent.*
import kotlin.random.Random

private const val otherThreadCount = 4
private val workers = Array(otherThreadCount) { Worker.start() }

actual fun <T> dispatchThread(block: () -> T): T {
    if(otherThreadCount == 0)
        block()

    val threadInt = Random.nextInt(otherThreadCount + 1)
    return if (threadInt == otherThreadCount)
        block()
    else {
        val future = workers[threadInt].execute(TransferMode.SAFE, { block.freeze() }) {
            try {
                it()
            } catch (e: Throwable) {
                e
            }
        }

        val res = future.result

        if(res is Throwable)
            throw res
        else
            return res as T
    }
}
